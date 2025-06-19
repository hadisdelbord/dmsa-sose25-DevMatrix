<template>
  <div>
<div class="mb-4">
  <label class="form-label">Select Charging Station</label>
  <select class="form-select" v-model="selectedStationId" @change="loadOfferSlots">
    <option disabled value="">Select a station</option>
    <option v-for="station in chargingStations" :key="station.id" :value="station.id">
      {{ station.name }} (ID: {{ station.id }})
    </option>
  </select>
</div>
<div class="mb-3">
  <label class="form-label">Select Date</label>
  <input type="date" class="form-control" v-model="selectedDate" />
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
        <tr v-for="(slot, index) in offerSlots" :key="index">
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

    <div class="d-flex justify-content-end mt-3">
      <button class="btn btn-primary" @click="saveSlots">Save Selected Slots</button>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue';
import offerSlotService from '@/service/OfferSlotService';
import chargingStationService from '@/service/StationManagementService';


const offerSlots = ref([]);
const chargingStations = ref([]);
const selectedStationId = ref('');
const selectedDate = ref('');

watch([selectedStationId, selectedDate], ([station, date]) => {
  if (station && date) {
    loadOfferSlots();
  }
});

const generateTimeSlots = () => {
    const slots = [];
  const startHour = 8;
  const endHour = 20;

  const selectedDateObj = new Date(selectedDate.value);
  if (isNaN(selectedDateObj)) return slots; // skip if date is invalid
  console.log(selectedDateObj);

  for (let hour = startHour; hour < endHour; hour++) {
    for (let min of [0, 30]) {
      const slotTime = new Date(selectedDateObj);
      slotTime.setHours(hour, min, 0, 0);
      const isoDateTime = slotTime.toISOString();

      slots.push({
        timeRange: `${String(hour).padStart(2, '0')}:${min === 0 ? '00' : '30'} - ${String(min === 0 ? hour : hour + 1).padStart(2, '0')}:${min === 0 ? '30' : '00'}`,
        timeSlot: hour + (min === 30 ? 0.5 : 0.0),
        slotDate: isoDateTime,                        
        chargingStationId: selectedStationId.value,   
        selected: false,
        pricePerSlot: 0,
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
 console.log(selectedDate.value);
  try {
    const response = await offerSlotService.getByStationIdAndDate(selectedStationId.value,  selectedDate.value);
    const existing = response.data;

    // Initialize all time slots
    const slots = generateTimeSlots();

    // Merge existing data into slots
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
      slotDuration:30
    }));
console.log(selected);
  if (selected.length === 0) {
    alert('Please select at least one slot.');
    return;
  }

  try {
    await offerSlotService.saveSlots(selected);
    alert('Offer slots saved!');
  } catch (error) {
    console.error('Failed to save slots:', error);
  }
};

onMounted(() => {
  loadChargingStations();
 
});
</script>