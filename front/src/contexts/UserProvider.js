import { createContext, useContext, useState, useEffect } from 'react'
import { useApi } from './ApiProvider'

const UserContext = createContext()

/*
    UserProvider Component
    -----------------------
    Provee un contexto para manejar el estado del usuario en la aplicación.
    Utiliza el ApiProvider para realizar operaciones de autenticación y obtener
    información del usuario actualmente autenticado.

    Usar los metodos login y logout para loguear o desloguear al usuario
    */

export default function UserProvider ({ children }) {
  const [user, setUser] = useState()

  const {api, authError} = useApi()

  useEffect(() => {
    ;(async () => {
      if (authError == false && api.isAuthenticated()) { // Checkea si existe token en local storage
        setUser("user") // Si el token es invalido se va a saber en cuanto se haga una request protegida
        
        if (localStorage.getItem('isAdmin') === "true"){
          const response = await api.get('/privileges')
          if (response.ok){
            setUser("admin")
          } else {
            setUser(null)
            localStorage.removeItem('isAdmin');
            localStorage.removeItem('accessToken');
          }
        }

      } else {
        setUser(null)
      }

    })()
  }, [authError])

  const login = async (username, password) => {
    const result = await api.login(username, password)
    if (result === 'ok') {
        const response = await api.get('/privileges')
        const role = response.ok ? 'admin' : 'user';
        setUser(role);
        localStorage.setItem('isAdmin', response.ok ? 'true' : 'false');

    }
    return result
  }

  const logout = async () => {
    setUser(null)
    localStorage.removeItem('accessToken');
    localStorage.removeItem('isAdmin');
  }

  const isAdmin = () => {
    return user === "admin"
  }

  return (
    <UserContext.Provider value={{ user, setUser, login, logout, isAdmin }}>
      {children}
    </UserContext.Provider>
  )
}

export function useUser () {
  return useContext(UserContext)
}
