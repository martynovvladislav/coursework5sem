import React, {useEffect} from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ApartmentDetailPage from './pages/ApartmentDetailPage';
import ProfilePage from './pages/ProfilePage';
import PrivateRoute from "./components/PrivateRoute.jsx";
import ApartmentFormPage from './pages/ApartmentForPage.jsx';

const App = () => {
  useEffect(() => {
    const checkLoginExpiration = () => {
      const loginTime = localStorage.getItem('loginTime');
      if (loginTime) {
        const currentTime = new Date().getTime();
        const elapsedTime = currentTime - loginTime;

        if (elapsedTime > 86400000) {
          localStorage.removeItem('token');
          localStorage.removeItem('role');
          localStorage.removeItem('loginTime');
        }
      }
    };

    checkLoginExpiration();

  }, []);


  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/apartments/:id" element={<ApartmentDetailPage />} />
        <Route
          path="/profile"
          element={
            <PrivateRoute>
              <ProfilePage />
            </PrivateRoute>
          }/>
          <Route path='apartments/create' element={<ApartmentFormPage />} />
          <Route path='apartments/edit/:apartmentId' element={<ApartmentFormPage />} />
      </Routes>
    </Router>
  );
};

export default App;
