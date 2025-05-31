<template>
  <div class="container my-4">
    <h2 class="mb-4">All Bookings</h2>

    <div v-if="bookings.length > 0">
      <table class="table table-bordered table-striped">
        <thead class="table-light">
          <tr>
            <th>ID</th>
            <th>Offer ID</th>
            <th>User ID</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="booking in bookings" :key="booking.bookingId">
            <td>{{ booking.bookingId }}</td>
            <td>{{ booking.offerId }}</td>
            <td>{{ booking.userId }}</td>
            <td>
              <span class="badge bg-secondary">{{ booking.bookingStatus }}</span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else class="alert alert-warning text-center" role="alert">
      No bookings found.
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import BookingService from '../service/BookingService';

const bookings = ref([]);

onMounted(async () => {
  try {
    const response = await BookingService.getAll();
    bookings.value = response.data;
  } catch (error) {
    console.error('Failed to load bookings:', error.message);
  }
});
</script>
