<template>
  <div>
    <h4 class="mb-4">Bookings for Your Stations</h4>

    <input type="search" v-model="searchQuery" class="form-control mb-3" placeholder="Search by station name..."
      autocomplete="off" />

    <table class="table table-hover" v-if="!loading && bookings.length">
      <thead class="table-light">
        <tr>
          <th>Booking ID</th>
          <th>Status</th>
          <th>Station</th>
          <th>Date</th>
          <th>Time Slot</th>
          <th>Price</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="booking in paginatedBookings" :key="booking.bookingId">
          <td>{{ booking.bookingId }}</td>
          <td>
            <span class="badge" :class="{
              'bg-warning': booking.status === 'RESERVED',
              'bg-success': booking.status === 'CONFIRMED',
              'bg-secondary': booking.status === 'COMPLETED',
              'bg-danger': booking.status === 'CANCELED',
            }">
              {{ booking.status }}
            </span>
          </td>
          <td>{{ booking.station }}</td>
          <td>{{ booking.slotDate }}</td>
          <td>{{ booking.timeSlot }}</td>
          <td>{{ booking.price }} €</td>
          <td>
            <button v-if="booking.status === 'RESERVED'" class="btn btn-sm btn-danger"
              @click="cancelBooking(booking.bookingId)">
              Cancel
            </button>
            <span v-else class="text-muted">—</span>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="loading" class="text-center my-4">Loading bookings...</div>
    <div v-if="!loading && bookings.length === 0" class="text-center my-4">No bookings found.</div>

    <!-- Pagination controls -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <button class="page-link" @click="goToPage(currentPage - 1)">Previous</button>
        </li>
        <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: page === currentPage }">
          <button class="page-link" @click="goToPage(page)">{{ page }}</button>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <button class="page-link" @click="goToPage(currentPage + 1)">Next</button>
        </li>
      </ul>
    </nav>

    <!-- Cancel confirm modal -->
    <div class="modal fade" id="cancelConfirmModal" tabindex="-1" aria-labelledby="cancelConfirmModalLabel"
      aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="cancelConfirmModalLabel">Confirm Cancellation</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            Are you sure you want to cancel Booking #{{ selectedBookingId }}?
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
            <button type="button" class="btn btn-danger" @click="confirmCancel">Yes, Cancel</button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="alertMessage" class="alert alert-info mt-3">{{ alertMessage }}</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import * as bootstrap from 'bootstrap';

// services
import stationService from '@/service/StationManagementService.js';
import bookingService from '@/service/BookingService.js';
import offerSlotService from '@/service/OfferSlotService.js';

const user = JSON.parse(localStorage.getItem('user'));
const ownerId = user?.userId;

const bookings = ref([]);
const searchQuery = ref('');
const loading = ref(false);
const alertMessage = ref('');
const selectedBookingId = ref(null);
let modalInstance = null;

// pagination state
const currentPage = ref(1);
const itemsPerPage = ref(5);

// fetch bookings
const fetchBookings = async () => {
  if (!ownerId) {
    alertMessage.value = 'User not logged in.';
    return;
  }
  loading.value = true;
  try {
    const stationsRes = await stationService.getStationsByOwnerId(ownerId);
    const stations = stationsRes.data;

    const allBookings = [];

    for (const station of stations) {
      const offerIds = station.offerSlots?.map(slot => slot.id) || [];
      if (offerIds.length === 0) continue;

      const bookingsRes = await bookingService.getByOfferIds(offerIds);
      const stationBookings = bookingsRes.data;

      for (const booking of stationBookings) {
        const offer = station.offerSlots.find(o => o.id === booking.offerId);

        allBookings.push({
          bookingId: booking.bookingId,
          offerId: booking.offerId,
          status: booking.bookingStatus,
          station: station.name,
          slotDate: offer?.slotDate ? offer.slotDate.split('T')[0] : 'N/A',
          timeSlot: offer?.timeSlot || 'N/A',
          price: offer?.pricePerSlot || 0,
        });
      }
    }

    bookings.value = allBookings;
    // alertMessage.value = 'Bookings loaded successfully!';
  } catch (error) {
    console.error('Error fetching bookings:', error);
    alertMessage.value = 'Failed to load bookings.';
  } finally {
    loading.value = false;
  }
};

// computed
const filteredBookings = computed(() => {
  if (!searchQuery.value) return bookings.value;
  return bookings.value.filter(b =>
    b.station.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const totalPages = computed(() =>
  Math.ceil(filteredBookings.value.length / itemsPerPage.value)
);

const paginatedBookings = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  const end = start + itemsPerPage.value;
  return filteredBookings.value.slice(start, end);
});

// pagination actions
const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

// cancel logic
const cancelBooking = (bookingId) => {
  selectedBookingId.value = bookingId;
  const modalEl = document.getElementById('cancelConfirmModal');
  modalInstance = new bootstrap.Modal(modalEl);
  modalInstance.show();
};

const confirmCancel = async () => {
  const booking = bookings.value.find(b => b.bookingId === selectedBookingId.value);
  if (!booking) {
    alertMessage.value = 'Booking not found.';
    modalInstance.hide();
    return;
  }

  if (booking.status !== 'RESERVED') {
    alertMessage.value = 'Only RESERVED bookings can be canceled.';
    modalInstance.hide();
    return;
  }

  try {
    await bookingService.cancel(booking.bookingId);
    await offerSlotService.UpdateOfferSlot(booking.offerId, { is_available: true });
    booking.status = 'CANCELED';
    alertMessage.value = `Booking #${booking.bookingId} canceled successfully.`;
  } catch (error) {
    console.error('Failed to cancel booking:', error);
    alertMessage.value = 'Failed to cancel booking.';
  } finally {
    modalInstance.hide();
  }
};

onMounted(fetchBookings);
</script>
