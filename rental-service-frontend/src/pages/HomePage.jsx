import React, { useEffect, useState } from 'react';
import { fetchApartments } from '../api/index.js';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../components/Header.jsx';
import '../styles/HomePage.css';

const HomePage = () => {
  const [apartments, setApartments] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const loadApartments = async () => {
      try {
        const { data } = await fetchApartments();
        setApartments(data);
      } catch (error) {
        console.error('Error fetching apartments:', error);
        setError('Failed to load apartments');
      }
    };

    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);

    loadApartments();
  }, []);

  return (
    <div className="home-container">
      <Header />

      <h1>Published Apartments</h1>
      {error && <p>{error}</p>}
      <ul className="apartment-list">
        {apartments.map((apartment) => (
          <li key={apartment.id} className="apartment-card">
            <img
              src={`http://localhost:8090${apartment.photoUrl}`}
              alt={apartment.title}
              className="apartment-image"
            />
            <div className="apartment-info">
              <h2>{apartment.title}</h2>
              <p>{apartment.address}</p>
              <p>{apartment.pricePerMonth}$ per month</p>
              <a href={`/apartments/${apartment.id}`} className="details-link">
                View details
              </a>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default HomePage;
