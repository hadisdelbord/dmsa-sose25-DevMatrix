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

      <!-- Filter by date -->
      <div class="mb-3">
        <label class="form-label">Filter by date:</label>
        <input type="date" v-model="filterDate" class="form-control" style="max-width: 200px;">
      </div>

      <table class="table mb-4">
        <thead>
          <tr>
            <th>Station</th>
            <th>Power Output</th>
            <th>City</th>
            <th>Street</th>
            <th>Postal Code</th>
            <th>Timeslot</th>
            <th>Date</th>
            <th>Price (€)</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="offer in paginatedOffers" :key="offer.offerId">
            <td>{{ offer.stationName }}</td>
            <td>{{ offer.powerOutput }}</td>
            <td>{{ offer.address.city }}</td>
            <td>{{ offer.address.street }}</td>
            <td>{{ offer.address.postalCode.value }}</td>
            <td>{{ offer.timeSlot }}</td>
            <td>{{ offer.availableDate }}</td>
            <td>{{ offer.price }}</td>
            <td>
              <button class="btn btn-sm btn-primary" @click="confirmBooking(offer)">Book</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Pagination controls -->
      <nav aria-label="Offers pagination">
        <ul class="pagination">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="changePage(currentPage - 1)">Previous</button>
          </li>
          <li class="page-item disabled">
            <span class="page-link">
              Page {{ currentPage }} of {{ totalPages }}
            </span>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="changePage(currentPage + 1)">Next</button>
          </li>
        </ul>
      </nav>
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
import StationService from "@/service/StationManagementService.js";

export default {
  name: 'LocationMap',
  data() {
    return {
      map: null,
    };
  },
  async mounted() {
    this.initMap();

    // postal code → lat/lng mapping (mock data)
  const postalCodeCoords = {
  "44135": { latitude: 51.5135, longitude: 7.4675 },
  "44137": { latitude: 51.5110, longitude: 7.4650 },
  "44139": { latitude: 51.5075, longitude: 7.4655 },
  "44141": { latitude: 51.5090, longitude: 7.4700 },
  "44143": { latitude: 51.5150, longitude: 7.4750 },
  "44145": { latitude: 51.5210, longitude: 7.4720 },
  "44147": { latitude: 51.5190, longitude: 7.4560 },
  "44149": { latitude: 51.5095, longitude: 7.4360 },
  "44225": { latitude: 51.4960, longitude: 7.4570 },
  "44227": { latitude: 51.4920, longitude: 7.4720 },
  "44229": { latitude: 51.4750, longitude: 7.4630 },
  "44263": { latitude: 51.4900, longitude: 7.5000 },
  "44265": { latitude: 51.4760, longitude: 7.5120 },
  "44267": { latitude: 51.4660, longitude: 7.5250 },
  "44269": { latitude: 51.4680, longitude: 7.5410 },
  "44287": { latitude: 51.4965, longitude: 7.5530 },
  "44289": { latitude: 51.4920, longitude: 7.5700 },
  "44309": { latitude: 51.5290, longitude: 7.5450 },
  "44319": { latitude: 51.5295, longitude: 7.5800 },
  "44328": { latitude: 51.5500, longitude: 7.5500 },
  "44329": { latitude: 51.5450, longitude: 7.5900 },
  "44339": { latitude: 51.5430, longitude: 7.4800 },
  "44357": { latitude: 51.5550, longitude: 7.4300 },
  "44359": { latitude: 51.5650, longitude: 7.4200 },
  "44369": { latitude: 51.5200, longitude: 7.4000 },
  "44379": { latitude: 51.5150, longitude: 7.4400 },
  "44388": { latitude: 51.5050, longitude: 7.3800 },
  "44399": { latitude: 51.5300, longitude: 7.5300 },
  // add more if you want...
};


    const fakeStations = [
      {
        name: 'Fake Station 1',
        address: {
          city: 'Dortmund',
          street: 'Musterstraße 1'
        },
        latitude: 51.5136,
        longitude: 7.4653
      },
      {
        name: 'Fake Station 2',
        address: {
          city: 'Dortmund',
          street: 'Beispielweg 5'
        },
        latitude: 51.5150,
        longitude: 7.4670
      }
    ];

    try {
      const response = await StationService.getAllStationsForMap();

      if (Array.isArray(response.data) && response.data.length > 0) {
        // enrich stations with lat/lng
        const enrichedStations = response.data.map(station => {
          const postalCode = station.address?.postalCode?.value;
          const coords = postalCodeCoords[postalCode];
          if (coords) {
            return {
              stationName: station.name,
              city: station.address.city,
              street: station.address.street,
              latitude: coords.latitude,
              longitude: coords.longitude
            };
          } else {
            console.warn(`No coordinates found for postal code: ${postalCode}`);
            return null;
          }
        }).filter(s => s !== null); // remove stations without coords

        if (enrichedStations.length > 0) {
          this.loadLocations(enrichedStations);
        } else {
          console.warn('All stations missing coords → using fake data');
          this.loadLocations(fakeStations);
        }
      } else {
        console.warn('StationService.GetAll() returned empty or invalid data → using fake data');
        this.loadLocations(fakeStations);
      }
    } catch (error) {
      console.error('Error fetching stations:', error);
      this.loadLocations(fakeStations);
    }
  },
  methods: {
    initMap() {
      this.map = L.map('map').setView([51.5136, 7.4653], 13); // Dortmund center
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap contributors',
      }).addTo(this.map);
    },
    loadLocations(stations) {
      stations.forEach(station => {
        if (station.latitude && station.longitude) {
          L.marker([station.latitude, station.longitude], {
            icon: L.icon({
              iconUrl: chargerIcon,
              iconSize: [30, 30],
              iconAnchor: [15, 30],
              popupAnchor: [0, -30],
            })
          })
            .addTo(this.map)
            .bindPopup(`
            <b>${station.stationName || station.name || 'Unnamed Station'}</b><br/>
            ${station.city || ''}, ${station.street || ''}
          `);
        }
      });
    },
  },
};
</script>

<script setup>
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import { Toast } from 'bootstrap'
import bookingService from "@/service/BookingService.js";
import paymentService from "@/service/PaymentService.js";
import offerSlotService from "@/service/OfferSlotService.js";

// User data
const user = JSON.parse(localStorage.getItem('user'));
const userId = user?.userId;

// State variables
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

// Filter & pagination state
const filterDate = ref('');
const currentPage = ref(1);
const itemsPerPage = ref(5);

// Computed: filtered offers by date
const filteredOffers = computed(() => {
  if (!filterDate.value) return offers.value;
  return offers.value.filter(offer => offer.availableDate === filterDate.value);
});

// Computed: paginated offers
const paginatedOffers = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value;
  return filteredOffers.value.slice(start, start + itemsPerPage.value);
});

// Total pages
const totalPages = computed(() => {
  return Math.ceil(filteredOffers.value.length / itemsPerPage.value) || 1;
});

// Change page helper
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
}

// Reset page to 1 when filter changes
watch(filterDate, () => {
  currentPage.value = 1;
});

// Confirm booking
const confirmBooking = (offer) => {
  selectedOffer.value = offer
  showModal.value = true
}

// Close modal
const closeModal = () => {
  showModal.value = false
  selectedOffer.value = null
}

// Submit booking
const submitBooking = async () => {
  const offer = selectedOffer.value;

  try {
    await offerSlotService.UpdateOfferSlot(offer.offerId, {
      isAvailable: false
    });
  } catch (error) {
    console.error('Failed to update offer:', error)
    showToast('Error updating offer availability')
  }

  try {
    await bookingService.create({
      offerId: offer.offerId,
      userId: userId,
      bookingStatus: 'RESERVED'
    });
    showToast('Booking created!');
    loadUserBookings();
    fetchOffers();
  } catch (error) {
    console.error('Failed to create booking:', error);
    showToast('Error creating booking');
  }

  closeModal();
}

// Load user bookings
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

// Submit payment
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
    bookingDate: new Date().toISOString().split('T')[0],
    paymentMethod: paymentMethod.value
  };

  try {
    await paymentService.createPayment(payment);
    await bookingService.confirm(booking.bookingId);

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

// Fetch offers
const fetchOffers = async () => {
  try {
    const code = searchCode.value.trim();
    if (!code) {
      showToast('Please enter a postal code');
      return;
    }

    const response = await offerSlotService.getAvailableOffers(code);

    offers.value = response.data.map(offer => {
      const formattedPowerOutput = `${offer.powerOutput} kW`;
      const date = offer.availableDate.split('T')[0];
      return { ...offer, powerOutput: formattedPowerOutput, availableDate: date };
    });
  } catch (error) {
    console.error('Error fetching offers:', error);
    showToast('Failed to fetch offers');
  }
};



// On mount
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
