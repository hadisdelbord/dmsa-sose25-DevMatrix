<template>
  <div>
    <h4 class="mb-4">My Bookings</h4>

    <!-- Search by Postal Code -->
    <div class="mb-4">
      <label class="form-label"><strong>Search by Postal Code</strong></label>
      <input v-model="searchCode" class="form-control" placeholder="Enter postal code..." />
    </div>

    <!-- Offers Found -->
    <div v-if="filteredOffers.length">
      <h5>Available Offers</h5>
      <div class="list-group mb-4">
        <div class="list-group-item d-flex justify-content-between align-items-start flex-column"
          v-for="offer in filteredOffers" :key="offer.offerId">
          <div>
            <strong>{{ offer.stationName }}</strong> ({{ offer.powerOutput }})<br />
            Address: {{ offer.address.city }}, {{ offer.address.street }} ({{ offer.address.postalCode }})<br />
            Timeslot: {{ offer.timeslot }} | Date: {{ offer.availableDate }} | Price: {{ offer.price }} €
          </div>
          <div class="text-end mt-2">
            <button class="btn btn-sm btn-primary" @click="confirmBooking(offer)">Book</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Confirmation Modal -->
    <div v-if="showModal" class="modal-backdrop">
      <div class="modal-content bg-white p-4 rounded shadow">
        <h5>Confirm Booking</h5>
        <p>Do you want to book the offer at <strong>{{ selectedOffer.stationName }}</strong> on {{
          selectedOffer.availableDate }}?</p>
        <div class="text-end">
          <button class="btn btn-success me-2" @click="submitBooking">Yes, Book</button>
          <button class="btn btn-secondary" @click="closeModal">Cancel</button>
        </div>
      </div>
    </div>

    <!-- Payment Modal -->
    <div v-if="showPaymentModal" class="modal-backdrop">
      <div class="modal-content bg-white p-4 rounded shadow">
        <h5>Payment for Booking ID: {{ paymentBooking.bookingId }}</h5>
        <p>Amount to pay: <strong>{{ paymentBooking.price }} €</strong></p>

        <div class="mb-3">
          <label class="form-label">Select Payment Method</label>
          <select v-model="paymentMethod" class="form-select">
            <option disabled value="">-- Select payment method --</option>
            <option value="Credit Card">Credit Card</option>
            <option value="PayPal">PayPal</option>
            <option value="Other">Other</option>
          </select>
        </div>

        <div class="text-end">
          <button class="btn btn-success me-2" @click="submitPayment">Submit Payment</button>
          <button class="btn btn-secondary" @click="closePaymentModal">Cancel</button>
        </div>
      </div>
    </div>

    <!-- Bookings Table -->
    <h5>Your Bookings</h5>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Booking ID</th>
          <th>Status</th>
          <th>Station</th>
          <th>Date</th>
          <th>Timeslot</th>
          <th>Price</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="booking in myBookings" :key="booking.bookingId">
          <td>{{ booking.bookingId }}</td>
          <td>
            <span :class="{
              'badge bg-warning text-dark': booking.status === 'RESERVED',
              'badge bg-success': booking.status === 'CONFIRMED'
            }">
              {{ booking.status }}
            </span>
          </td>
          <td>{{ booking.stationName }}</td>
          <td>{{ booking.date }}</td>
          <td>{{ booking.timeslot }}</td>
          <td>{{ booking.price }} €</td>
          <td>
            <button v-if="booking.status === 'RESERVED'" class="btn btn-sm btn-primary"
              @click="openPaymentModal(booking)">
              Pay
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Bootstrap Toast Notification -->
    <div class="position-fixed top-0 end-0 p-3" style="z-index: 1100">
      <div ref="toastRef" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive"
        aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body">{{ toastMessage }}</div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"
            @click="hideToast"></button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { Toast } from 'bootstrap'

const userId = 42

const searchCode = ref('')
const showModal = ref(false)
const showPaymentModal = ref(false)
const selectedOffer = ref(null)
const paymentBooking = ref(null)
const paymentMethod = ref('')

const toastRef = ref(null)
const toastInstance = ref(null)
const toastMessage = ref('')

const offers = ref([
  {
    offerId: 201,
    stationName: 'FastCharge A',
    powerOutput: '50kW',
    timeslot: '30 Minutes',
    availableDate: '2025-06-01',
    price: 8.5,
    address: { city: 'Addis', street: 'Main St', postalCode: '1000' }
  },
  {
    offerId: 202,
    stationName: 'EV Hub',
    powerOutput: '75kW',
    timeslot: '1 Hour',
    availableDate: '2025-06-02',
    price: 12.0,
    address: { city: 'Adama', street: 'Power Rd', postalCode: '1100' }
  }
])

const myBookings = ref([])

const filteredOffers = computed(() =>
  offers.value.filter(offer =>
    offer.address.postalCode.includes(searchCode.value.trim())
  )
)

const confirmBooking = (offer) => {
  selectedOffer.value = offer
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  selectedOffer.value = null
}

const submitBooking = () => {
  const offer = selectedOffer.value
  myBookings.value.push({
    bookingId: Date.now(),
    offerId: offer.offerId,
    userId: userId,
    status: 'RESERVED',
    stationName: offer.stationName,
    timeslot: offer.timeslot,
    date: offer.availableDate,
    price: offer.price
  })

  closeModal()
  showToast('Booking successful!')
}

// Payment modal handlers
const openPaymentModal = (booking) => {
  paymentBooking.value = booking
  paymentMethod.value = ''
  showPaymentModal.value = true
}

const closePaymentModal = () => {
  showPaymentModal.value = false
  paymentBooking.value = null
  paymentMethod.value = ''
}

const submitPayment = () => {
  if (!paymentMethod.value) {
    showToast('Please select a payment method')
    return
  }

  // Update booking status to CONFIRMED
  const index = myBookings.value.findIndex(b => b.bookingId === paymentBooking.value.bookingId)
  if (index !== -1) {
    myBookings.value[index].status = 'CONFIRMED'
  }

  showToast(`Payment successful via ${paymentMethod.value}!`)
  closePaymentModal()
}

// Toast handlers
function showToast(message) {
  toastMessage.value = message
  nextTick(() => {
    if (!toastInstance.value && toastRef.value) {
      toastInstance.value = new Toast(toastRef.value)
    }
    toastInstance.value.show()
  })
}

function hideToast() {
  if (toastInstance.value) toastInstance.value.hide()
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-content {
  width: 100%;
  max-width: 400px;
}

/* Toast styling overrides if needed */
</style>
