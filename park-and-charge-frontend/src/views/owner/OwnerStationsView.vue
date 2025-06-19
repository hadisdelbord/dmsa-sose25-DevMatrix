<template>
  <div>
    <h4 class="mb-4">List of The Stations</h4>

    <!-- Search Bar -->
    <input
      type="search"
      v-model="searchStation"
      class="form-control mb-3"
      placeholder="Search by postal code..."
      autocomplete="off"
    />
    <div class="d-flex justify-content-end mb-3">
      <button class="btn btn-success" @click="openAddModal()">
        <i class="bi bi-plus-lg me-1"></i> Create Station
      </button>
    </div>
    <!-- Stations Table -->
    <table class="table table-hover">
      <thead class="table-light">
        <tr>
          <th>Station ID</th>
          <th>Station Name</th>
          <th>Status</th>
          <th>Power Output</th>
          <th>User</th>
          <th>Postal Code</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="station in filteredStation" :key="station.id">
          <td>{{ station.id }}</td>
          <td>{{ station.name }}</td>
          <td>
            <span
              class="badge"
              :class="{
                'bg-success': station.activityStatus === 'ACTIVE',
                'bg-danger': station.activityStatus === 'Inactive'
              }"
            >
              {{ station.activityStatus }}
            </span>
          </td>
          <td>{{ station.powerOutput }}</td>
          <td>{{ station.userId }}</td>
          <td>{{ station.address.postalCode.value }}</td>
          <td>
            <button
              class="btn btn-sm btn-primary"
              @click="openEditModal(station)"
            >
              Edit
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- Edit Modal -->
   <div
    class="modal fade"
    id="stationModal"
    tabindex="-1"
    aria-labelledby="stationModalLabel"
    aria-hidden="true"
    ref="modalRef"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">

        <div class="modal-header">
          <h5 class="modal-title" id="stationModalLabel">
            {{ isEditMode ? 'Update Station' : 'Add New Station' }}
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>

        <div class="modal-body">
          <div class="border rounded p-3">
<div class="mb-3">
    <label class="form-label">Postal Code</label>
  <select
    class="form-select"
    v-model="formData.station.address"
  >
    <option disabled value="">Select postal code</option>
    <option
      v-for="addr in allAddresses"
      :key="addr.id"
      :value="addr"
    >
      {{ addr.postalCode?.value }} - {{ addr.city }}, {{ addr.street }}
    </option>
  </select>
</div>
          <div class="row">
              <div class="col-md-6">            
              <label class="form-label">Station Name</label>
              <input
              type="text"
              class="form-control"
              v-model="formData.station.name"
              placeholder="Enter the station name"
              />
            </div>
            <div class="col-md-6">            
              <label class="form-label">Power Output (kW)</label>
              <input
              type="number"
              class="form-control"
              v-model="formData.station.powerOutput"
              placeholder="Enter power output"
              />
            </div>
            <div class="col-md-6">
              <label class="form-label">Status</label>
              <select
              class="form-select"
              v-model="formData.station.activityStatus"
              >
              <option value="ACTIVE">Active</option>
              <option value="Inactive">Inactive</option>
              </select>
            </div>
             </div>
          </div>

        </div>
        
        <div class="modal-footer">
          <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-primary" @click="handleSubmit">
            {{ isEditMode ? 'Update' : 'Create' }}
          </button>
        </div>

      </div>

    </div>
  </div>

  <!-- Confirm Cancel Modal -->
  <div
    class="modal fade"
    id="cancelConfirmModal"
    tabindex="-1"
    aria-labelledby="cancelConfirmModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="cancelConfirmModalLabel">
            Confirm Cancellation
          </h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          Are you sure you want to cancel Booking #{{ selectedBookingId }}?
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            No
          </button>
          <button type="button" class="btn btn-danger" @click="confirmCancel">
            Yes, Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import * as bootstrap from 'bootstrap';
import stationService from '@/service/StationManagementService';
import locationService from '@/service/LocationService';

//let modalInstance = null;
const selectedBookingId = ref(null);
//const alertMessage = ref('');
const searchStation = ref('');
const stations = ref([]);
let modalInstance = null;
const modalRef = ref(null); 
const isEditMode = ref(false);
const submitted = ref(false);
const allAddresses = ref([]);
const formData = ref({
  station: {},
  address: {postalCode: { code: '' }}
});


onMounted(async () => {
  try {
    console.log("Here");
    const response = await stationService.getAll();
    stations.value = response.data;
    console.log(stations.value) // assuming your API returns an array
  } catch (error) {
    console.error('Failed to load stations:', error);
  }
});

const filteredStation = computed(() => {
  if (!searchStation.value) return stations.value;
  return stations.value.filter((station) =>
    station.address.postalCode.value.toLowerCase().includes(searchStation.value.toLowerCase())
  );
});
const openAddModal = () => {
  isEditMode.value = false;
    formData.value = {
    station: {
      name: '',
      powerOutput: 0,
      activityStatus: 'ACTIVE',
      userId: ''
    }
  };
   formData.value.address = {};
  showModal();
};

const openEditModal = (station) => {
  isEditMode.value = true;
  formData.value.station = { ...station };
  formData.value.address = { ...station.address };
  showModal();
};

const showModal = () => {
  if (!modalInstance) {
    modalInstance = new bootstrap.Modal(modalRef.value);
  }
  modalInstance.show();
};

onMounted(async () => {
  try {
    const response = await locationService.getAll();
    allAddresses.value = response.data;
    console.log('Loaded addresses:', allAddresses.value);
  } catch (error) {
    console.error('Failed to load addresses:', error);
  }
});


const handleSubmit = async () => {
  submitted.value = true;

  const station = formData.value.station;
  console.log(station);
  // Basic required field check
  if (!station.addressId && !station.address?.id) {
    console.warn("Address is required.");
    return;
  }

  try {
    await stationService.updateStation(station, isEditMode.value);

    // Refresh station list
    const response = await stationService.getAll();
    stations.value = response.data;

    // Reset form and state
    formData.value.station = getEmptyStation(); // define this factory like getEmptyLocation()
    submitted.value = false;
    modalInstance.hide();

  } catch (error) {
    console.error('Failed to save station:', error);
    // Optionally show a toast or message
  }
};
const getEmptyStation = () => ({
  name: '',
  powerOutput: 0,
  activityStatus: 'ACTIVE',
  addressId: null // or address: null if passing full object
});
// const cancelBooking = (id) => {
//   selectedBookingId.value = id;
//   const modalEl = document.getElementById('cancelConfirmModal');
//   modalInstance = new bootstrap.Modal(modalEl);
//   modalInstance.show();
// };

// const confirmCancel = () => {
//   const booking = stationBookings.value.find(
//     (b) => b.bookingId === selectedBookingId.value
//   );
//   if (booking && booking.status === 'RESERVED') {
//     booking.status = 'CANCELED';
//     alertMessage.value = `Booking #${booking.bookingId} has been canceled.`;
//   } else {
//     alertMessage.value = `Booking #${booking.bookingId} cannot be canceled.`;
//   }

//   modalInstance.hide();
//};
</script>
