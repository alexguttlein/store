import Container from 'react-bootstrap/Container'
import Header from './components/common/Header'
import PrivateRoute from './components/routes/PrivateRoute'
import PublicRoute from './components/routes/PublicRoute'
import AdminRoute from './components/routes/AdminRoute'

import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AboutPage from './pages/AboutPage'
import SignUpPage from './pages/SignUpPage'
import LoginPage from './pages/LoginPage'
import ConfirmPage from './pages/ConfirmPage'
import ProductsListPage from './pages/ProductsListPage'
import ProductPage from './pages/ProductPage'
import NewProductPage from './pages/NewProductPage'
import CartPage from './pages/CartPage'
import ProductListProvider from './contexts/ProductListProvider'
import ProductProvider from './contexts/ProductProvider'
import OrderListProvider from './contexts/OrderListProvider'
import OrderListPage from './pages/OrderListPage'
import ProviderWrapper from './contexts/ProviderWrapper'
import ResetRequestPage from './pages/ResetRequestPage'
import ResetPage from './pages/ResetPage'

/*
    Componente principal. Se encarga de manejar las rutas publicas y privadas de la aplicación.
    Si se crea un provider para un contexto, se debe añadir aca como parent de todas las rutas que vayan a usarlo
*/

function App () {
  const useMockData = false
  const usePagination = true

  return (
    <Container fluid className='App'>
      <BrowserRouter>
        <ProviderWrapper>
          <header>
            <Header />
          </header>

          <main>
            <Routes>
              {/* Agregar rutas publicas aca */}

              <Route
                path='/'
                element={
                  <PublicRoute>
                    <AboutPage />
                  </PublicRoute>
                }
              />
              <Route
                path='/about'
                element={
                  <PublicRoute>
                    <AboutPage />
                  </PublicRoute>
                }
              />
              <Route
                path='/login'
                element={
                  <PublicRoute>
                    <LoginPage />
                  </PublicRoute>
                }
              />
              <Route
                path='/sign-up'
                element={
                  <PublicRoute>
                    <SignUpPage />
                  </PublicRoute>
                }
              />
              <Route
                path='/confirm'
                element={
                  <PublicRoute>
                    <ConfirmPage />
                  </PublicRoute>
                }
              />
              <Route
                path='/reset'
                element={
                  <PublicRoute>
                    <ResetPage />
                  </PublicRoute>
                }
              />
              <Route
                path='/reset-request'
                element={
                  <PublicRoute>
                    <ResetRequestPage />
                  </PublicRoute>
                }
              />
              <Route
                path='*'
                element={
                  <PrivateRoute>
                    <Routes>
                      {/* Agregar rutas privadas aca */}

                      <Route path='/cart' element={<CartPage />} />

                      <Route
                        path='/products'
                        element={
                          <ProductListProvider
                            mock={useMockData}
                            pagination={usePagination}
                          >
                            {' '}
                            <ProductsListPage />
                          </ProductListProvider>
                        }
                      />
                      <Route
                        path='/order'
                        element={
                          <OrderListProvider
                            mock={useMockData}
                          >
                            {' '}
                            <OrderListPage />
                          </OrderListProvider>
                        }
                      />
                      <Route
                        path='/products/new'
                        element={
                          <AdminRoute>
                            <ProductProvider mock={useMockData}>
                              {' '}
                              <NewProductPage />
                            </ProductProvider>
                          </AdminRoute>
                        }
                      />

                      <Route
                        path='/products/:productId'
                        element={
                          <ProductProvider mock={useMockData}>
                            {' '}
                            <ProductPage />
                          </ProductProvider>
                        }
                      />
                    </Routes>
                  </PrivateRoute>
                }
              />
            </Routes>
          </main>
        </ProviderWrapper>
      </BrowserRouter>
    </Container>
  )
}

export default App
