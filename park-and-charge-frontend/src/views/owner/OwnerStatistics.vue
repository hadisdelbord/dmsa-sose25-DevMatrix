<template>
  <div>
    <h2 class="mb-4">Owner Statistics</h2>
    <!-- Stats Circles -->
    <div class="d-flex justify-content-center gap-4 mb-4">
      <div class="text-center" style="width: 120px;">
        <div class="rounded-circle bg-primary bg-opacity-25 text-primary fw-bold d-flex align-items-center justify-content-center"
            style="width: 100px; height: 100px; font-size: 1.3rem; margin: 0 auto;">
          {{ totalPrice.toFixed(2) }} €
        </div>
        <div class="mt-2">Total Price</div>
      </div>
      <div class="text-center" style="width: 120px;">
        <div class="rounded-circle bg-success bg-opacity-25 text-success fw-bold d-flex align-items-center justify-content-center"
            style="width: 100px; height: 100px; font-size: 1.3rem; margin: 0 auto;">
          {{ totalUsageKWh }} kWh
        </div>
        <div class="mt-2">Total Usage</div>
      </div>
      <div class="text-center" style="width: 120px;">
        <div class="rounded-circle bg-info bg-opacity-25 text-info fw-bold d-flex align-items-center justify-content-center"
            style="width: 100px; height: 100px; font-size: 1.3rem; margin: 0 auto;">
          {{ pricePerKWh.toFixed(2) }} €/kWh
        </div>
        <div class="mt-2">Avg Price</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import StatisticsService from '@/service/StatisticsService'

// DATA
const ownerId = '123' // (for now, hardcoded)
const bookings = ref([])
const totalPrice = ref(0)
const totalUsageKWh = ref(0)
const pricePerKWh = ref(0)
const stationNames = ref([])

// FETCH initial data
const fetchStats = async () => {
  try {
    const res = await StatisticsService.getAll(ownerId)
    bookings.value = res.data.bookings
    totalPrice.value = res.data.totalPrice
    totalUsageKWh.value = res.data.totalUsageKWh
    pricePerKWh.value = res.data.pricePerKWh
    stationNames.value = res.data.stationNames
  } catch (err) {
    console.error('Failed to fetch stats:', err)
  }
}

onMounted(fetchStats)
</script>
