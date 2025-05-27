<template>
  <div align="center" style="margin-top: 80px">
    <table cellpadding="50" cellspacing="0" style="border: 1px solid black; border-radius: 8px">
      <tr>
        <td>
          <form @submit.prevent="loginUser">
            <table cellpadding="10" cellspacing="0">
              <tr>
                <td colspan="2" align="center">
                  <h2>User Login</h2>
                </td>
              </tr>
              <tr>
                <td align="left"><b>Email:</b></td>
                <td align="left">
                  <input v-model="email" type="email" required />
                </td>
              </tr>
              <tr>
                <td align="left"><b>Password:</b></td>
                <td align="left">
                  <input v-model="password" type="password" required />
                </td>
              </tr>
              <tr>
                <td colspan="2" align="center">
                  <button type="submit">Login</button>
                </td>
              </tr>
              <tr v-if="message">
                <td colspan="2" align="center">
                  <p>{{ message }}</p>
                </td>
              </tr>
              <tr>
                <td colspan="2" align="center">
                  <p>Don't have an account? <a href="/register">Register</a></p>
                </td>
              </tr>
            </table>
          </form>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      email: '',
      password: '',
      message: '',
    }
  },
  methods: {
    async loginUser() {
      try {
        const response = await axios.post('http://localhost:8080/users/login', {
          email: this.email,
          password: this.password,
        })
        this.message = `Welcome ${response.data.name}!`
      } catch (error) {
        this.message = error.response?.data?.message || 'Login failed.'
      }
    },
  },
}
</script>
