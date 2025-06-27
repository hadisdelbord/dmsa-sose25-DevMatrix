<template>
  <div>
    <h4 class="mb-4">Bookings for Your Stations</h4>

    <input
      type="search"
      v-model="searchQuery"
      class="form-control mb-3"
      placeholder="Search by station name..."
      autocomplete="off"
    />

    <table class="table table-hover" v-if="!loading && bookings.length">
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

    <div v-if="loading" class="text-center my-4">Loading bookings...</div>
    <div v-if="!loading && bookings.length === 0" class="text-center my-4">No bookings found.</div>

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

    <div v-if="alertMessage" class="alert alert-info mt-3">{{ alertMessage }}</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import * as bootstrap from 'bootstrap';

// Services you need to implement or already have:
import stationService from '@/service/StationManagementService.js'; // to get stations by ownerId
import bookingService from '@/service/BookingService.js'; // bookings by stationId
import offerSlotService from '@/service/OfferSlotService.js'; // offer + station details

const user = JSON.parse(localStorage.getItem('user'));
const ownerId = user?.userId;

const bookings = ref([]);
const searchQuery = ref('');
const loading = ref(false);
const alertMessage = ref('');
const selectedBookingId = ref(null);
let modalInstance = null;

const fetchBookingsForOwnerStations = async () => {
  if (!ownerId) {
    alertMessage.value = 'User not logged in.';
    return;
  }

  loading.value = true;
  try {
    // 1. Get stations owned by user
    const stationsRes = await stationService.getStationsByOwnerId(ownerId);
    const stations = stationsRes.data;

    // 2. For each station get bookings
    const allBookings = [];
    for (const station of stations) {
      try {
        const bookingsRes = await bookingService.getByStationId(station.stationId);
        const stationBookings = bookingsRes.data;

        // Enrich bookings with offer & station data and user info
        const enrichedBookings = await Promise.all(
          stationBookings.map(async (booking) => {
            try {
              const offerRes = await offerSlotService.getOfferAndStationByOfferId(
                booking.offerId
              );
              const offer = offerRes.data;

              return {
                bookingId: booking.bookingId,
                offerId: booking.offerId,
                status: booking.bookingStatus || booking.status,
                station: station.stationName,
                user: booking.user || { name: 'Unknown', email: '' },
                slotDate: offer.availableDate
                  ? offer.availableDate.split('T')[0]
                  : 'N/A',
                timeSlot: offer.timeSlot || 'N/A',
                price: offer.price || 0,
              };
            } catch (err) {
              console.error(
                `Failed to get offer for booking ${booking.bookingId}:`,
                err
              );
              return {
                bookingId: booking.bookingId,
                offerId: booking.offerId,
                status: booking.bookingStatus || booking.status,
                station: station.stationName,
                user: booking.user || { name: 'Unknown', email: '' },
                slotDate: 'N/A',
                timeSlot: 'N/A',
                price: 0,
              };
            }
          })
        );

        allBookings.push(...enrichedBookings);
      } catch (error) {
        console.error(`Failed to fetch bookings for station ${station.stationId}:`, error);
      }
    }

    bookings.value = allBookings;
    alertMessage.value = 'Bookings loaded successfully!';
  } catch (error) {
    console.error('Failed to fetch stations or bookings:', error);
    alertMessage.value = 'Failed to load bookings.';
  } finally {
    loading.value = false;
  }
};

const filteredBookings = computed(() => {
  if (!searchQuery.value) return bookings.value;
  return bookings.value.filter((b) =>
    b.station.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const cancelBooking = (bookingId) => {
  selectedBookingId.value = bookingId;
  const modalEl = document.getElementById('cancelConfirmModal');
  modalInstance = new bootstrap.Modal(modalEl);
  modalInstance.show();
};

const confirmCancel = async () => {
  const booking = bookings.value.find((b) => b.bookingId === selectedBookingId.value);
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
    // Cancel booking in backend
    await bookingService.updateBookingStatus(booking.bookingId, 'CANCELED');

    // Mark offer available again
    await offerSlotService.updateOfferAvailability(booking.offerId, {
      is_available: true,
    });

    // Update locally
    booking.status = 'CANCELED';
    alertMessage.value = `Booking #${booking.bookingId} has been canceled.`;
  } catch (error) {
    console.error('Failed to cancel booking:', error);
    alertMessage.value = 'Failed to cancel booking.';
  } finally {
    modalInstance.hide();
  }
};

onMounted(() => {
  fetchBookingsForOwnerStations();
});
</script>
