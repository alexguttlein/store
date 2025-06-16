import { Navigate } from 'react-router-dom';
import { useUser } from '../../contexts/UserProvider';

/*
  Encapsula rutas privadas
*/

export default function AdminRoute({ children }) {
  const { user, isAdmin } = useUser();

  if (user === undefined) {
    return null;
  }
  else if (user && isAdmin()) {
    return children;
  }
  else {
    return <Navigate to="/products" replace={true}/>
  }
}