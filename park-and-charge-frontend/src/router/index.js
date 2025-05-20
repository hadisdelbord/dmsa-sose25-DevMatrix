import { createRouter, createWebHistory } from 'vue-router'
import Bookings from '../views/Booking.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Bookings',
      component: Bookings
    },
  ],
})

export default router
