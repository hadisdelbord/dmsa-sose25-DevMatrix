<template>
  <div>
    <h2 class="mb-4">Owner Statistics</h2>

    <!-- Stats Circles -->
    <div class="d-flex justify-content-center gap-4 mb-4">
      <div class="text-center" style="width: 120px;">
        <div
          class="rounded-circle bg-warning bg-opacity-25 text-warning fw-bold d-flex align-items-center justify-content-center"
          style="width: 100px; height: 100px; font-size: 1.3rem; margin: 0 auto;"
        >
          {{ filteredBookings.length }}
        </div>
        <div class="mt-2">Total Bookings</div>
      </div>
      <div class="text-center" style="width: 120px;">
        <div
          class="rounded-circle bg-primary bg-opacity-25 text-primary fw-bold d-flex align-items-center justify-content-center"
          style="width: 100px; height: 100px; font-size: 1.3rem; margin: 0 auto;"
        >
          {{ totalPrice.toFixed(2) }} €
        </div>
        <div class="mt-2">Total Price</div>
      </div>
      <div class="text-center" style="width: 120px;">
        <div
          class="rounded-circle bg-secondary bg-opacity-25 text-secondary fw-bold d-flex align-items-center justify-content-center"
          style="width: 100px; height: 100px; font-size: 1.3rem; margin: 0 auto;"
        >
          {{ avgBookingPrice.toFixed(2) }} €
        </div>
        <div class="mt-2">Avg Booking Price</div>
      </div>

    </div>

    <!-- Filters -->
    <div class="d-flex flex-wrap gap-3 align-items-center justify-content-between mb-4">
      <div v-if="stationNames.length">
        <label class="fw-semibold me-2">Stations:</label>
        <div>
          <label>
            <input
              type="checkbox"
              :checked="selectedStations.length === stationNames.length"
              @change="toggleAllStations"
            /> All
          </label>
          <div v-for="station in stationNames" :key="station">
            <label>
              <input type="checkbox" :value="station" v-model="selectedStations" /> {{ station }}
            </label>
          </div>
        </div>
      </div>

      <div>
        <label class="fw-semibold me-2">Time:</label>
        <select class="form-select d-inline-block w-auto" v-model="selectedDateRange">
          <option v-for="opt in timeOptions" :value="opt.value" :key="opt.value">{{ opt.label }}</option>
        </select>
      </div>

      <div>
        <button class="btn btn-primary me-2" @click="applyFilters">Apply</button>
        <button class="btn btn-outline-secondary" @click="resetFilters">Reset Filters</button>
      </div>
    </div>

    <!-- Booking table -->
    <table class="table table-bordered table-hover mt-4">
      <thead class="table-light">
        <tr>
          <th>Station</th>
          <th>Date</th>
          <th>Status</th>
          <th>Price (€)</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="b in filteredBookings" :key="b.bookingId">
          <td>{{ b.station }}</td>
          <td>{{ b.slotDate }}</td>
          <td>
            <span
              class="badge"
              :class="{
                'bg-warning': b.status === 'RESERVED',
                'bg-success': b.status === 'CONFIRMED',
                'bg-secondary': b.status === 'COMPLETED',
                'bg-danger': b.status === 'CANCELED',
              }"
            >
              {{ b.status }}
            </span>
          </td>
          <td>{{ b.price.toFixed(2) }}</td>
        </tr>
        <tr v-if="filteredBookings.length === 0">
          <td colspan="6" class="text-center text-muted">No bookings found for the selected filter.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import stationService from '@/service/StationManagementService.js'
import bookingService from '@/service/BookingService.js'
import offerSlotService from '@/service/OfferSlotService.js'

const user = JSON.parse(localStorage.getItem('user'))
const ownerId = user?.userId || null

// Raw bookings array
const bookings = ref([])

// Filters
const stationNames = ref([])
const selectedStations = ref([])
const selectedDateRange = ref('all')

const timeOptions = [
  { value: 'all', label: 'All Time' },
  { value: 'last_year', label: 'Last Year' },
  { value: 'last_6_months', label: 'Last 6 Months' },
  { value: 'last_3_months', label: 'Last 3 Months' },
  { value: 'last_month', label: 'Last Month' },
  { value: 'last_week', label: 'Last Week' },
  { value: 'last_day', label: 'Last Day' },
]

// Fetch all bookings for stations owned by user
const fetchBookings = async () => {
  if (!ownerId) {
    bookings.value = []
    stationNames.value = []
    return
  }

  try {
    const stationsRes = await stationService.getStationsByOwnerId(ownerId)
    const stations = stationsRes.data

    stationNames.value = stations.map(s => s.name)
    selectedStations.value = [...stationNames.value]

    const allBookings = []

    for (const station of stations) {
      const offerIds = station.offerSlots?.map(slot => slot.id) || []
      if (!offerIds.length) continue

      const bookingsRes = await bookingService.getByOfferIds(offerIds)
      const stationBookings = bookingsRes.data

      for (const booking of stationBookings) {
        const offer = station.offerSlots.find(o => o.id === booking.offerId)
        if (!offer) continue

        // Usage kWh is not part of your booking data; I'll simulate or pull from offer if available.
        // Assuming offer has usageKWh field or else zero:
        const usageKWh = offer.usageKWh || 0

        // price per kWh = total price / usageKWh if usageKWh > 0 else 0
        const pricePerKWh = usageKWh > 0 ? offer.pricePerSlot / usageKWh : 0

        allBookings.push({
          bookingId: booking.bookingId,
          offerId: booking.offerId,
          status: booking.bookingStatus,
          station: station.name,
          slotDate: offer.slotDate ? offer.slotDate.split('T')[0] : 'N/A',
          timeSlot: offer.timeSlot || 'N/A',
          price: offer.pricePerSlot || 0,
          usageKWh,
          pricePerKWh,
        })
      }
    }

    bookings.value = allBookings
  } catch (err) {
    console.error('Error loading bookings for stats:', err)
  }
}

// Filtering function for date range
const filterByDateRange = (booking, range) => {
  if (range === 'all') return true

  const bookingDate = new Date(booking.slotDate)
  const now = new Date()

  switch (range) {
    case 'last_year':
      return bookingDate >= new Date(now.getFullYear() - 1, now.getMonth(), now.getDate())
    case 'last_6_months':
      return bookingDate >= new Date(now.getFullYear(), now.getMonth() - 6, now.getDate())
    case 'last_3_months':
      return bookingDate >= new Date(now.getFullYear(), now.getMonth() - 3, now.getDate())
    case 'last_month':
      return bookingDate >= new Date(now.getFullYear(), now.getMonth() - 1, now.getDate())
    case 'last_week':
      return bookingDate >= new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
    case 'last_day':
      return bookingDate >= new Date(now.getTime() - 24 * 60 * 60 * 1000)
    default:
      return true
  }
}

// Computed filtered bookings by station and date range
const filteredBookings = computed(() => {
  return bookings.value.filter(b => {
    const inStation = selectedStations.value.includes(b.station)
    const inDateRange = filterByDateRange(b, selectedDateRange.value)
    return inStation && inDateRange
  })
})

// Computed total price
const totalPrice = computed(() => {
  return filteredBookings.value.reduce((sum, b) => sum + b.price, 0)
})

const avgBookingPrice = computed(() => {
  if (filteredBookings.value.length === 0) return 0
  return totalPrice.value / filteredBookings.value.length
})


// Handlers
const toggleAllStations = () => {
  if (selectedStations.value.length === stationNames.value.length) {
    selectedStations.value = []
  } else {
    selectedStations.value = [...stationNames.value]
  }
}

const applyFilters = () => {
  // Just triggers reactivity — computed properties automatically update
}

const resetFilters = () => {
  selectedStations.value = [...stationNames.value]
  selectedDateRange.value = 'all'
}

onMounted(fetchBookings)
</script>
