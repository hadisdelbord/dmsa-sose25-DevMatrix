<template>
  <div align="center" style="margin-top: 80px">
    <table cellpadding="50" cellspacing="0" style="border: 1px solid black; border-radius: 8px">
      <tr>
        <td>
          <form @submit.prevent="registerUser">
            <table cellpadding="10" cellspacing="0">
              <tr>
                <td colspan="2" align="center">
                  <h2>User Registration</h2>
                </td>
              </tr>
              <tr>
                <td align="left">
                  <b>Name: <span style="color: red">*</span></b>
                </td>
                <td align="left">
                  <input v-model="name" type="text" />
                </td>
              </tr>
              <tr>
                <td align="left">
                  <b>Email: <span style="color: red">*</span></b>
                </td>
                <td align="left">
                  <input v-model="email" type="email" required />
                </td>
              </tr>
              <tr>
                <td align="left">
                  <b>Password: <span style="color: red">*</span></b>
                </td>
                <td align="left">
                  <input v-model="password" type="password" required />
                </td>
              </tr>
              <tr>
                <td align="left">
                  <b>Role: <span style="color: red">*</span></b>
                </td>
                <td align="left">
                  <input type="radio" id="driver" value="DRIVER" v-model="role" />
                  <label for="driver"><b>Driver</b></label
                  ><br />
                  <input type="radio" id="owner" value="OWNER" v-model="role" />
                  <label for="owner"><b>Owner</b></label>
                </td>
              </tr>
              <tr>
                <td colspan="2" align="center">
                  <button type="submit">Register</button>
                </td>
              </tr>
              <tr v-if="message">
                <td colspan="2" align="center">
                  <p>{{ message }}</p>
                </td>
              </tr>
              <tr>
                <td colspan="2" align="center">
                  <p>Already have an account? <a href="/login">Login</a></p>
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
      name: '',
      email: '',
      password: '',
      role: 'DRIVER',
      message: '',
    }
  },
  methods: {
    async registerUser() {
      try {
        const response = await axios.post('http://localhost:8080/users/register', {
          name: this.name,
          email: this.email,
          password: this.password,
          role: this.role,
        })
        this.message = 'Registration successful!'
      } catch (error) {
        this.message = error.response?.data?.message || 'Registration failed.'
      }
    },
  },
}
</script>
