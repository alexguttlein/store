import { createContext, useContext, useState } from 'react';
import ApiClient from '../ApiClient';

const ApiContext = createContext();

export default function ApiProvider({ children }) {
  const [authError, setAuthError] = useState(false)

  const handleAuthError = () => {
    setAuthError(true)
  }

  const api = new ApiClient(handleAuthError);

  return (
    <ApiContext.Provider value={{api, authError}}>
      {children}
    </ApiContext.Provider>
  );
}

export function useApi() {
  return useContext(ApiContext);
}