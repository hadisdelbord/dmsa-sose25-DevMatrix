<template>
  <div>
    <h4 class="mb-4">Bookings for Your Stations</h4>

    <!-- Search Bar -->
    <input
      type="search"
      v-model="searchQuery"
      class="form-control mb-3"
      placeholder="Search by station name..."
      autocomplete="off"
    />

    <!-- Bookings Table -->
    <table class="table table-hover">
      <thead class="table-light">
        <tr>
          <th>Booking ID</th>
          <th>Status</th>
          <th>Station</th>
          <th>User</th>
          <th>Date</th>
          <th>Time Slot</th>
          <th>Price</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="booking in filteredBookings" :key="booking.bookingId">
          <td>{{ booking.bookingId }}</td>
          <td>
            <span
              class="badge"
              :class="{
                'bg-warning': booking.status === 'RESERVED',
                'bg-success': booking.status === 'CONFIRMED',
                'bg-secondary': booking.status === 'COMPLETED',
                'bg-danger': booking.status === 'CANCELED',
              }"
            >
              {{ booking.status }}
            </span>
          </td>
          <td>{{ booking.station }}</td>
          <td>{{ booking.user.name }} ({{ booking.user.email }})</td>
          <td>{{ booking.slotDate }}</td>
          <td>{{ booking.timeSlot }}</td>
          <td>{{ booking.price }} €</td>
          <td>
            <button
              v-if="booking.status === 'RESERVED'"
              class="btn btn-sm btn-danger"
              @click="cancelBooking(booking.bookingId)"
            >
              Cancel
            </button>
            <span v-else class="text-muted">—</span>
          </td>
        </tr>
      </tbody>
    </table>
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
import { ref, computed } from 'vue';
import * as bootstrap from 'bootstrap';
import { faker } from '@faker-js/faker';

let modalInstance = null;
const selectedBookingId = ref(null);
const alertMessage = ref('');
const searchQuery = ref('');

// Generate dummy bookings programmatically
const generateBookings = (count = 10) => {
  const statuses = ['RESERVED', 'CONFIRMED', 'COMPLETED', 'CANCELED'];
  const stations = ['FastCharge A', 'EV Hub', 'ChargePoint', 'SuperCharge'];
  const bookings = [];

  for (let i = 0; i < count; i++) {
    bookings.push({
      bookingId: 2000 + i,
      status: statuses[Math.floor(Math.random() * statuses.length)],
      station: stations[Math.floor(Math.random() * stations.length)],
      slotDate: faker.date.future().toISOString().slice(0, 16).replace('T', ' '),
      timeSlot: ['30 Minutes', '60 Minutes', '90 Minutes'][Math.floor(Math.random() * 3)],
      price: (10 + Math.random() * 10).toFixed(2),
      user: {
        name: faker.name.firstName(),
        email: faker.internet.email(),
      },
    });
  }
  return bookings;
};

const stationBookings = ref(generateBookings(10));

const filteredBookings = computed(() => {
  if (!searchQuery.value) return stationBookings.value;
  return stationBookings.value.filter((booking) =>
    booking.station.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const cancelBooking = (id) => {
  selectedBookingId.value = id;
  const modalEl = document.getElementById('cancelConfirmModal');
  modalInstance = new bootstrap.Modal(modalEl);
  modalInstance.show();
};

const confirmCancel = () => {
  const booking = stationBookings.value.find(
    (b) => b.bookingId === selectedBookingId.value
  );
  if (booking && booking.status === 'RESERVED') {
    booking.status = 'CANCELED';
    alertMessage.value = `Booking #${booking.bookingId} has been canceled.`;
  } else {
    alertMessage.value = `Booking #${booking.bookingId} cannot be canceled.`;
  }

  modalInstance.hide();
};
</script>
