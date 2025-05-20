<template>
  <div class="booking-container">
    <h2 class="title">All Bookings</h2>

    <table v-if="bookings.length > 0" class="bookings-table">
      <thead>
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
          <td>{{ booking.bookingStatus }}</td>
        </tr>
      </tbody>
    </table>

    <p v-else class="empty-msg">No bookings found.</p>
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
    console.error("Failed to load bookings:", error.message);
  }
});
</script>

<style scoped>
.booking-container {
  padding: 2rem;
  font-family: sans-serif;
}

.title {
  text-align: left;
  font-size: 2rem;
  margin-bottom: 1.5rem;
}

.bookings-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 1.1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.bookings-table th,
.bookings-table td {
  border: 1px solid #ddd;
  padding: 1rem;
  text-align: left;
}

.bookings-table th {
  background-color: #f8f8f8;
  font-weight: bold;
}

.bookings-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.empty-msg {
  margin-top: 2rem;
  color: #888;
  font-size: 1.2rem;
  text-align: center;
}
</style>
