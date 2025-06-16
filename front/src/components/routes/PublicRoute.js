import { Navigate } from 'react-router-dom';
import { useUser } from '../../contexts/UserProvider';

/*
  Encapsula rutas publicas
*/

export default function PublicRoute({ children }) {
  const { user } = useUser();

  if (user === undefined) {
    return null;
  }
  else if (user) {
    return <Navigate to="/products" replace={true}/>
  }
  else {
    return children;
  }
}