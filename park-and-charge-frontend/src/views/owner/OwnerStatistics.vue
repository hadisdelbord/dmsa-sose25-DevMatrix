<template>
  <div>
    <h2 class="mb-4">Owner Statistics</h2>
    <!-- Next: stats circles, filters, table -->
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
