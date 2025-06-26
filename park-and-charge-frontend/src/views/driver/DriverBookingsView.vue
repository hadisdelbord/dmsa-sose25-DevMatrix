<template>
  <div>
    <h4 class="mb-4">My Bookings</h4>

    <!-- Search by Postal Code -->
    <div class="mb-4">
      <label class="form-label"><strong>Search by Postal Code</strong></label>
      <input v-model="searchCode" class="form-control" placeholder="Enter postal code..." @change="fetchOffers" />
    </div>

    <!-- Map -->
    <div id="map" style="height: 400px; margin: 20px;"></div>

    <!-- Offers Found -->
    <div v-if="offers.length">
      <h5>Available Offers</h5>
      <div class="list-group mb-4">
        <div class="list-group-item d-flex justify-content-between align-items-start flex-column"
          v-for="offer in offers" :key="offer.offerId">
          <div>
            <strong>{{ offer.stationName }}</strong> ({{ offer.powerOutput }})<br />
            Address: {{ offer.address.city }}, {{ offer.address.street }} ({{ offer.address.postalCode.value }})<br />
            Timeslot: {{ offer.timeSlot }} | Date: {{ offer.availableDate }} | Price: {{ offer.price }} €
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
              'badge bg-warning text-dark': booking.bookingStatus === 'RESERVED',
              'badge bg-success': booking.bookingStatus === 'CONFIRMED'
            }">
              {{ booking.bookingStatus }}
            </span>
          </td>
          <td>{{ booking.stationName }}</td>
          <td>{{ booking.date }}</td>
          <td>{{ booking.timeslot }}</td>
          <td>{{ booking.price }} €</td>
          <td>
            <button v-if="booking.bookingStatus === 'RESERVED'" class="btn btn-sm btn-primary"
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


<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import chargerIcon from '@/assets/icons/charger-1.png';
import mapService from "@/service/MapService.js";
import offerSlotService from "@/service/OfferSlotService.js";

export default {
  name: 'LocationMap',
  data() {
    return {
      map: null,
    };
  },
  async mounted() {
    // Init Map
    this.initMap();

    // Get All locations and show on map
    const response = await mapService.getAllLocations();
    this.loadLocations(response.data);
  },
  methods: {
    initMap() {
      this.map = L.map('map').setView([51.5136, 7.4653], 13); // Default center: Dortmund
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap contributors',
      }).addTo(this.map);
    },
    loadLocations(data) {
      try {
        data.forEach(loc => {
          if (loc.latitude && loc.longitude) {
            L.marker([loc.latitude, loc.longitude], {
              icon: L.icon({
                iconUrl: chargerIcon,
                iconSize: [30, 30],
                iconAnchor: [22, 94],
                popupAnchor: [-3, -76],
              })
            })
              .addTo(this.map)
              .bindPopup(`<b>${loc.city}, ${loc.street}</b>`);
          }
        });
      } catch (error) {
        console.error("Error loading locations:", error);
      }
    },
  },
};
</script>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { Toast } from 'bootstrap'
import bookingService from "@/service/BookingService.js";
import paymentService from "@/service/PaymentService.js";

const user = JSON.parse(localStorage.getItem('user'));
const userId = user?.userId;


const searchCode = ref('')
const showModal = ref(false)
const showPaymentModal = ref(false)
const selectedOffer = ref(null)
const paymentBooking = ref(null)
const paymentMethod = ref('')

const toastRef = ref(null)
const toastInstance = ref(null)
const toastMessage = ref('')
const offers = ref([])



const myBookings = ref([])


const confirmBooking = (offer) => {
  selectedOffer.value = offer
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
  selectedOffer.value = null
}

const submitBooking = async () => {
  const offer = selectedOffer.value;

  // Update offer availability via API (currently commented for fake data)
  try {
     await offerSlotService.UpdateOfferSlot(offer.offerId, {
      isAvailable: false
    });
    // showToast('Offer availability updated!')
  } catch (error) {
    console.error('Failed to update offer:', error)
    showToast('Error updating offer availability')
  }

  // Call bookingService.create() to create a new booking
  try {
    await bookingService.create({
      offerId: offer.offerId,
      userId: userId,
      bookingStatus: 'RESERVED'
    });
    showToast('Booking created via API!');
    loadUserBookings();
    fetchOffers();

  } catch (error) {
    console.error('Failed to create booking:', error);
    showToast('Error creating booking');
  }


  closeModal();
  showToast('Booking successful!');
}

//loadbookings
const loadUserBookings = async () => {
  if (!userId) return;

  try {
    const response = await bookingService.getByUserId(userId);
    const bookings = response.data;

    const enrichedBookings = await Promise.all(
      bookings.map(async (booking) => {
        try {
          const offerResponse = await offerSlotService.getOfferAndStationByOfferId(booking.offerId);
          const offer = offerResponse.data;

          const date = offer.availableDate?.split('T')[0] ?? 'N/A';

          return {
            bookingId: booking.bookingId,
            offerId: booking.offerId,
            userId: booking.userId,
            bookingStatus: booking.bookingStatus,
            stationName: offer.stationName,
            timeslot: offer.timeSlot,
            date: date,
            price: offer.price
          };
        } catch (error) {
          console.error(`Failed to load offer for booking ${booking.bookingId}:`, error);
          return {
            ...booking,
            stationName: 'Unavailable',
            timeslot: 'N/A',
            date: 'N/A',
            price: 0
          };
        }
      })
    );

    myBookings.value = enrichedBookings;
    showToast('Bookings loaded!');
  } catch (error) {
    console.error('Failed to load bookings:', error);
    showToast('Error loading your bookings');
  }
};




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


const submitPayment = async () => {
  if (!paymentMethod.value) {
    showToast('Please select a payment method');
    return;
  }

  const booking = paymentBooking.value;
  const index = myBookings.value.findIndex(b => b.bookingId === booking.bookingId);

  if (index === -1) {
    showToast('Booking not found');
    return;
  }

  const payment = {
    bookingId: booking.bookingId,
    bookingAmount: booking.price,
    bookingDate: new Date().toISOString().split('T')[0], // e.g., "2025-06-19"
    paymentMethod: paymentMethod.value
  };

  try {
    await paymentService.createPayment(payment);
    await bookingService.confirm(booking.bookingId); // Update booking status on backend

    // Update local status after backend confirms
    myBookings.value[index].status = 'CONFIRMED';

    showToast(`Payment successful via ${paymentMethod.value}!`);
    loadUserBookings();
  } catch (error) {
    console.error('Failed to process payment or confirm booking:', error);
    showToast('Error processing payment');
  }

  closePaymentModal();
};

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

// Prepared API Call (for fetching offers - commented)
const fetchOffers = async () => {
  try {
    const response = await offerSlotService.getAvailableOffers(searchCode.value.trim());

    offers.value = response.data.map(offer => {
      const formattedPowerOutput = `${offer.powerOutput} kW`;

      // const startHour = Math.floor(offer.timeSlot);
      // console.log(startHour);
      // const startMinute = (offer.timeSlot % 1) * 60;
      // console.log(startMinute);
      // const duration =  offer.slotDuration;

      // const start = new Date(0, 0, 0, startHour, startMinute);
      // const end = new Date(start.getTime() + duration * 60000);
      // timeslot.value = `${start.toTimeString().substring(0, 5)} - ${end.toTimeString().substring(0, 5)}`;

      const date = offer.availableDate.split('T')[0];

      return {
        ...offer,
        powerOutput: formattedPowerOutput,
        availableDate: date
      };
    });

  } catch (error) {
    console.error('Error fetching offers:', error);
    showToast('Failed to fetch offers');
  }
}


onMounted(() => {
  loadUserBookings();
});
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
</style>
