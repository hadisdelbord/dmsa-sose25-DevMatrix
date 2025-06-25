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
    <!-- Filters Row -->
    <div class="d-flex flex-wrap gap-3 align-items-center justify-content-between mb-4">
      <!-- Stations Multi-checkbox -->
      <!-- <div v-if="stationNames.length">
        <label class="fw-semibold me-2">Stations:</label>
        <div class="dropdown d-inline">
          <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
            {{ selectedStations.length === 0 || selectedStations.length === stationNames.length ? 'All' : selectedStations.join(', ') }}
          </button>
          <div class="dropdown-menu p-2" style="min-width: 200px;">
            <div>
              <label>
                <input
                  type="checkbox"
                  :checked="selectedStations.length === stationNames.length"
                  @change="selectedStations = selectedStations.length === stationNames.length ? [] : stationNames.slice()"
                /> All
              </label>
            </div>
            <div v-for="station in stationNames" :key="station">
              <label>
                <input
                  type="checkbox"
                  :value="station"
                  v-model="selectedStations"
                /> {{ station }}
              </label>
            </div>
          </div>
        </div>
      </div> -->
      <div v-if="stationNames.length">
        <label class="fw-semibold me-2">Stations:</label>
        <div>
          <label>
            <input
              type="checkbox"
              :checked="selectedStations.length === stationNames.length"
              @change="selectedStations = selectedStations.length === stationNames.length ? [] : stationNames.slice()"
            /> All
          </label>
          <div v-for="station in stationNames" :key="station">
            <label>
              <input type="checkbox" :value="station" v-model="selectedStations" /> {{ station }}
            </label>
          </div>
        </div>
      </div>



      <!-- Time Dropdown -->
      <div>
        <label class="fw-semibold me-2">Time:</label>
        <select class="form-select d-inline-block w-auto" v-model="selectedDateRange">
          <option v-for="opt in timeOptions" :value="opt.value" :key="opt.value">
            {{ opt.label }}
          </option>
        </select>
      </div>

      <!-- Apply/Reset -->
      <div>
        <button class="btn btn-primary me-2" @click="applyFilters">Apply</button>
        <button class="btn btn-outline-secondary" @click="resetFilters">Reset Filters</button>
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

// FILTER STATE
const selectedStations = ref([])      // Stations selected by user
const selectedDateRange = ref('all')  // Selected time filter
const timeOptions = [
  { value: 'all', label: 'All Time' },
  { value: 'last_year', label: 'Last Year' },
  { value: 'last_6_months', label: 'Last 6 Months' },
  { value: 'last_3_months', label: 'Last 3 Months' },
  { value: 'last_month', label: 'Last Month' },
  { value: 'last_week', label: 'Last Week' },
  { value: 'last_day', label: 'Last Day' },
]

// Fetch initial stats from backend
const fetchStats = async () => {
  try {
    const res = await StatisticsService.getAll(ownerId)
    bookings.value = res.data.bookings
    totalPrice.value = res.data.totalPrice
    totalUsageKWh.value = res.data.totalUsageKWh
    pricePerKWh.value = res.data.pricePerKWh
    stationNames.value = res.data.stationNames
    console.log('Fetched stations:', stationNames.value)
  } catch (err) {
    console.error('Failed to fetch stats:', err)
  }
}

// Apply filters
const applyFilters = async () => {
  // If all or none are selected, send all stationNames
  const stationsToSend =
    selectedStations.value.length === 0 || selectedStations.value.length === stationNames.value.length
      ? stationNames.value
      : selectedStations.value

  try {
    const res = await StatisticsService.filter({
      ownerId,
      stationIds: stationsToSend,
      dateRange: selectedDateRange.value,
    })
    bookings.value = res.data.bookings
    totalPrice.value = res.data.totalPrice
    totalUsageKWh.value = res.data.totalUsageKWh
    pricePerKWh.value = res.data.pricePerKWh
    // Keep stationNames from initial fetch only
  } catch (err) {
    console.error('Failed to apply filters:', err)
  }
}

// Reset filters to "All"
const resetFilters = async () => {
  selectedStations.value = []
  selectedDateRange.value = 'all'
  await fetchStats()
}

onMounted(fetchStats)
</script>

