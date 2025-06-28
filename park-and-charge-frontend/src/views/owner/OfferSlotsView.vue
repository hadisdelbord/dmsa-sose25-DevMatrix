<template>
  <div>
    <div class="row">
      <div class="col-md-6">
        <label class="form-label">Select Charging Station</label>
        <select class="form-select" v-model="selectedStationId">
          <option disabled value="">Select a station</option>
          <option v-for="station in chargingStations" :key="station.id" :value="station.id">
            {{ station.name }}
          </option>
        </select>
      </div>

      <div class="col-md-6">
        <label class="form-label">Select Date</label>
        <input type="date" class="form-control" v-model="selectedDate" />
      </div>
    </div>

    <h4 class="mb-4">Manage Offer Slots</h4>

    <table class="table table-bordered">
      <thead class="table-light">
        <tr>
          <th>Select</th>
          <th>Time Slot</th>
          <th>Price (€)</th>
          <th>Available</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(slot, index) in paginatedSlots" :key="index">
          <td>
            <input type="checkbox" v-model="slot.selected" />
          </td>
          <td>{{ slot.timeRange }}</td>
          <td>
            <input
              type="number"
              step="0.5"
              class="form-control"
              v-model="slot.pricePerSlot"
              :disabled="!slot.selected"
            />
          </td>
          <td>
            <select class="form-select" v-model="slot.isAvailable" :disabled="!slot.selected">
              <option :value="true">Available</option>
              <option :value="false">Not Available</option>
            </select>
          </td>
        </tr>
      </tbody>
    </table>

    <nav class="d-flex justify-content-center">
      <ul class="pagination">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <button class="page-link" @click="currentPage--" :disabled="currentPage === 1">Previous</button>
        </li>

        <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
          <button class="page-link" @click="currentPage = page">{{ page }}</button>
        </li>

        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <button class="page-link" @click="currentPage++" :disabled="currentPage === totalPages">Next</button>
        </li>
      </ul>
    </nav>

    <div class="d-flex justify-content-end mt-3">
      <button class="btn btn-primary" @click="saveSlots">Save Selected Slots</button>
    </div>
  </div>

      <!-- Bootstrap Toast Notification -->
    <div class="position-fixed top-0 end-0 p-3" style="z-index: 1100">
      <div ref="toastRef" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive"
           aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body">{{ toastMessage }}</div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"
                  @click="hideToast"></button>
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, computed, nextTick } from 'vue';
import {Toast} from 'bootstrap'
import offerSlotService from '@/service/OfferSlotService';
import chargingStationService from '@/service/StationManagementService';

const offerSlots = ref([]);
const chargingStations = ref([]);
const selectedStationId = ref('');
const selectedDate = ref('');
const currentPage = ref(1);
const pageSize = 5;
const toastMessage = ref('');
const toastInstance = ref(null);
const toastRef = ref(null);

watch([selectedStationId, selectedDate], ([station, date]) => {
  if (station && date) {
    loadOfferSlots();
  }
});

const paginatedSlots = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return offerSlots.value.slice(start, start + pageSize);
});

const totalPages = computed(() => Math.ceil(offerSlots.value.length / pageSize));

const generateTimeSlots = () => {
  const slots = [];
  const startHour = 8;
  const endHour = 20;

  const selectedDateObj = new Date(selectedDate.value);
  if (isNaN(selectedDateObj)) return slots;

  for (let hour = startHour; hour < endHour; hour++) {
    for (let min of [0, 30]) {
      const slotTime = new Date(selectedDateObj);
      slotTime.setHours(hour, min, 0, 0);
      const isoDateTime = slotTime.toISOString();
      const timeRange = `${String(hour).padStart(2, '0')}:${min === 0 ? '00' : '30'} - ${String(min === 0 ? hour : hour + 1).padStart(2, '0')}:${min === 0 ? '30' : '00'}`;
      slots.push({
        timeRange,
        timeSlot: timeRange,
        slotDate: isoDateTime,
        chargingStationId: selectedStationId.value,
        selected: false,
        pricePerSlot: 50,
        isAvailable: true
      });
    }
  }
  return slots;
};

const loadChargingStations = async () => {
  try {
    const response = await chargingStationService.getAll();
    chargingStations.value = response.data;
  } catch (error) {
    console.error('Failed to load stations:', error);
  }
};

const loadOfferSlots = async () => {
  if (!selectedStationId.value) return;
  try {
    const response = await offerSlotService.getByStationIdAndDate(selectedStationId.value, selectedDate.value);
    const existing = response.data;
    const slots = generateTimeSlots();

    for (const slot of slots) {
      const match = existing.find(s => s.timeSlot === slot.timeSlot &&
        new Date(s.slotDate).toDateString() === new Date(slot.slotDate).toDateString());
      if (match) {
        slot.pricePerSlot = match.pricePerSlot;
        slot.isAvailable = match.isAvailable;
        slot.selected = true;
      }
    }

    offerSlots.value = slots;
    currentPage.value = 1;
  } catch (error) {
    console.error('Failed to load offer slots:', error);
  }
};

const saveSlots = async () => {
  const selected = offerSlots.value
    .filter(slot => slot.selected)
    .map(slot => ({
      timeSlot: slot.timeSlot,
      pricePerSlot: slot.pricePerSlot,
      isAvailable: slot.isAvailable,
      slotDate: slot.slotDate,
      stationId: selectedStationId.value,
      slotDuration: 30
    }));

  if (selected.length === 0) {
    showToast('Please select at least one slot.');
    return;
  }

  try {
    await offerSlotService.saveSlots(selected);
    showToast('Offer slots saved!')
  } catch (error) {
    console.error('Failed to save slots:', error);
  }
};

// Toast handlers
function showToast(message) {
  toastMessage.value = message
  nextTick(() => {
    if (!toastInstance.value && toastRef.value) {
      toastInstance.value = new Toast(toastRef.value)
    }
    toastInstance.value.show()
  })
}

function hideToast() {
  if (toastInstance.value) toastInstance.value.hide()
}

onMounted(() => {
  loadChargingStations();
});
</script>
