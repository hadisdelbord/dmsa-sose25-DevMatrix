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
import OwnerStationsView from '@/views/owner/OwnerStationsView.vue'
import OfferSlotsView from '@/views/owner/OfferSlotsView.vue'


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
        },
        {
          path: 'stations',
          name: 'OwnerStations',
          component: OwnerStationsView
        },
        {
          path: 'Offers',
          name: 'OfferSlots',
          component: OfferSlotsView
        }
      ]
    }
  ]
})
// Helper function to get current user
const getCurrentUser = () => {
  try {
    const userData = localStorage.getItem('user');
    if (!userData) {
      return null;
    }
    const user = JSON.parse(userData);
    
    // Check if user object has required properties
    if (!user.token || !user.email || !user.role) {
      console.warn('Invalid user data in localStorage, clearing...');
      localStorage.removeItem('user');
      return null;
    }
    
    return user;
  } catch (error) {
    console.error('Error parsing user data from localStorage:', error);
    localStorage.removeItem('user'); // Clear corrupted data
    return null;
  }
}


router.beforeEach((to, from, next) => {

  const user = getCurrentUser();
  const isAuthenticated = !!(user && user.token);
  const userRole = user?.role;

  console.log('User authentication status:', {
    isAuthenticated,
    userRole,
    email: user?.email,
    hasToken: !!user?.token,
    userExists: !!user
  });

  if (to.path === '/') {
    if (isAuthenticated) {
      const redirectPath = userRole === 'OWNER' ? '/owner' : '/driver';
      next(redirectPath);
    } else {
      next('/login');
    }
    return;
  }
  if (to.meta.guest) {
    if (isAuthenticated) {
      const redirectPath = userRole === 'OWNER' ? '/owner' : '/driver';
      next(redirectPath);
      return;
    }
    next();
    return;
  }
  if (to.meta.requiresAuth || to.path.startsWith('/owner') || to.path.startsWith('/driver')) {
    
    if (!isAuthenticated) {
      console.warn('Authentication required, redirecting to login');
      // Clear any invalid user data
      localStorage.removeItem('user');
      alert('Please login to access this page.');
      next('/login');
      return;
    }
    let requiredRole = to.meta.requiredRole;
    if (!requiredRole) {
      if (to.path.startsWith('/owner')) {
        requiredRole = 'OWNER';
      } else if (to.path.startsWith('/driver')) {
        requiredRole = 'DRIVER';
      }
    }
    if (requiredRole && userRole !== requiredRole) {
      console.warn(`Access denied: User role '${userRole}' does not match required role '${requiredRole}'`);
      const pageType = requiredRole === 'OWNER' ? 'Owner' : 'Driver';
      const userType = userRole === 'OWNER' ? 'Owner' : 'Driver';
      
      alert(`Access Denied!\n\nThis page is only accessible to ${pageType} users.\nYou are currently logged in as: ${userType}\n\nRedirecting to your dashboard...`);
      const redirectPath = userRole === 'OWNER' ? '/owner' : '/driver';
      next(redirectPath);
      return;
    }
  }
  next();
});
export default router
