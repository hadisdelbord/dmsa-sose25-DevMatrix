import { createRouter, createWebHistory } from 'vue-router'

// Auth
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'

// Layouts
import DriverLayout from '@/layouts/DriverLayout.vue'
import OwnerLayout from '@/layouts/OwnerLayout.vue'

// Driver Views
import DriverDashboard from '@/views/driver/DriverDashboard.vue'
import DriverBookingsView from '@/views/driver/DriverBookingsView.vue'

// Owner Views
import OwnerDashboard from '@/views/owner/OwnerDashboard.vue'
import OwnerBookingsView from '@/views/owner/OwnerBookingsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // Public routes
    {
      path: "/",
      component: Login
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },

    // Driver routes with layout
    {
      path: '/driver',
      component: DriverLayout,
      children: [
        {
          path: '',
          name: 'DriverDashboard',
          component: DriverDashboard
        },
        {
          path: 'bookings',
          name: 'DriverBookings',
          component: DriverBookingsView
        }
      ]
    },

    // Owner routes with layout
    {
      path: '/owner',
      component: OwnerLayout,
      children: [
        {
          path: '',
          name: 'OwnerDashboard',
          component: OwnerDashboard
        },
        {
          path: 'bookings',
          name: 'OwnerBookings',
          component: OwnerBookingsView
        }
      ]
    }
  ]
})

export default router
