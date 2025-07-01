<!-- src/layout/OwnerLayout.vue -->
<template>
<div class="d-flex min-vh-100">
    <!-- Sidebar -->
    <div class="bg-dark text-white p-3" style="width: 240px;">
      <h4 class="mb-4">Park And Charge</h4>
      <ul class="nav flex-column">
        <li class="nav-item mb-2">
          <router-link to="/owner" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-speedometer2 me-2"></i> Dashboard
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/owner/stations" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-hdd-network me-2"></i> Stations
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/owner/Offers" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-hdd-network me-2"></i> offerSlots
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/owner/bookings" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-calendar-check-fill me-2"></i> Bookings
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/owner/statistics" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-bar-chart-line-fill me-2"></i> Statistics
          </router-link>
        </li>
      </ul>
    </div>

    <!-- Main Content -->
    <div class="flex-grow-1">
      <div class="d-flex justify-content-between align-items-center bg-light border-bottom p-3">
        <h5 class="mb-0">Owner Dashboard</h5>
        <button class="btn btn-outline-danger btn-sm" @click="logout">Logout</button>
      </div>

      <div class="p-4">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
const logout = () => {
  // Optional: clear tokens or session data here
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
 const user = getCurrentUser();
if (user) {
    console.log('Logging out user:', user.email);
    
    // Method 1: Clear the token from the user object and update localStorage
    user.token = '';
    user.expired = true;
    user.loggedOut = new Date().toISOString();
    
    // Update localStorage with expired user (optional - for debugging)
    localStorage.setItem('user', JSON.stringify(user));
    
    // Method 2: Or just remove the user completely
    localStorage.removeItem('user');
  }
  
  // Clear all storage to be safe
  localStorage.clear();
  sessionStorage.clear();
    document.cookie.split(";").forEach(function(c) { 
    document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); 
  });
  alert('Logged out!');
  window.location.href = '/login';
};
</script>

<style scoped>
::v-deep(.router-link-active),
::v-deep(.router-link-exact-active) {
  background-color: #495057;
  border-radius: 5px;
}
</style>
