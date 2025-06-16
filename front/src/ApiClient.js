const BASE_API_URL = process.env.REACT_APP_BASE_API_URL

/*
    ApiClient Class
    ----------------
    Provee una interfaz para realizar http requests al server
    Abstrae la API `fetch`para facilitar operaciones basicas como GET, POST, PUT, DELETE y
    y otras como LOGIN, LOGOUT, etc.

    Los metodos de request devuelven un promise que en exito sera un objeto conteniendo los atributos:
    - `ok`: Indica si la operacion fue exitosa
    - `status`: Http status
    - `body`: Body de la respuesta como json

*/

export default class ApiClient {
  constructor (handleAuthError) {
    this.handleAuthError = handleAuthError
    this.base_url = BASE_API_URL
  }

  isAuthenticated () {
    return localStorage.getItem('accessToken') !== null
  }
  
  async request (options) {
    let query = new URLSearchParams(options.query || {}).toString()
    if (query !== '') {
      query = '?' + query
    }

    const headers = {
      'Content-Type': 'application/json',
      ...options.headers
    }

    if (!headers['Authorization'] && this.isAuthenticated()) {
      headers['Authorization'] = 'Bearer ' + localStorage.getItem('accessToken')
    }

    let response

    try {
      response = await fetch(this.base_url + options.url + query, {
        method: options.method,
        headers: headers,
        body: options.body ? JSON.stringify(options.body) : null
      })
      if (response.status === 401 && options.url !== '/login') {
        localStorage.removeItem('accessToken')
        this.handleAuthError()
      }
    } catch (error) {
      return  {
        ok: false,
        status: 500,
        body: {
          code: 500,
          message: 'The server is unresponsive',
          description: error.toString(),
        }
      }
    }

    let body = null;
  
    const responseBody = await response.text();
  
    try {
      body = responseBody ? JSON.parse(responseBody) : null;
    } catch (error) {
      body = responseBody;
    }

    return {
      ok: response.ok,
      status: response.status,
      body: response.status !== 204 
        ? body
        : null
    };
  }

  async get (url, query, options) {
    return this.request({ method: 'GET', url, query, ...options })
  }

  async post (url, body, options) {
    return this.request({ method: 'POST', url, body, ...options })
  }

  async put (url, body, options) {
    return this.request({ method: 'PUT', url, body, ...options })
  }

  async patch (url, body, options) {
    return this.request({ method: 'PATCH', url, body, ...options })
  }

  async delete (url, options) {
    return this.request({ method: 'DELETE', url, ...options })
  }

  async login (username, password) {
    const response = await this.post('/login', null, {
      headers: {
        Authorization: 'Basic ' + btoa(username + ':' + password)
      }
    })
    if (!response.ok) {
      return response.status === 401 ? 'fail' : 'error'
    }
    localStorage.setItem('accessToken', response.body.token)
    return 'ok'
  }

  logout () {
    localStorage.removeItem('accessToken')
  }
}