<template>
  <h2>Welcome,  {{ username }}!</h2>
  <!-- More content here -->
</template>

<script setup>
// User data
const user = JSON.parse(localStorage.getItem('user'));
const username = user?.name;

</script>
