<template>
<div class="d-flex min-vh-100">
    <!-- Sidebar -->
    <div class="bg-dark text-white p-3" style="width: 240px;">
      <h4 class="mb-4">Park And Charge</h4>
      <ul class="nav flex-column">
        <li class="nav-item mb-2">
          <router-link to="/driver" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-house-door-fill me-2"></i> Home
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/driver/bookings" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-calendar-check-fill me-2"></i> Bookings
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/profile" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-person-circle me-2"></i> Profile
          </router-link>
        </li>
        <li class="nav-item mb-2">
          <router-link to="/settings" class="nav-link text-white" exact-active-class="router-link-active">
            <i class="bi bi-gear-fill me-2"></i> Settings
          </router-link>
        </li>
      </ul>
    </div>

    <!-- Main content -->
    <div class="flex-grow-1">
      <div class="d-flex justify-content-between align-items-center bg-light border-bottom p-3">
        <h5 class="mb-0">Driver Dashboard</h5>
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
  // Optional: clear tokens or session storage
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
