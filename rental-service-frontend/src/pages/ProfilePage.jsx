import React, { useEffect, useState } from "react";
import {
  publishApartment,
  getBookings,
  fetchAllApartments,
  fetchMyApartments,
  deleteBooking,
  updateBookingStatus,
  deleteApartment,
  getBookingRequests,
} from "../api/index";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import "../styles/ProfilePage.css";

const ProfilePage = () => {
  const [role, setRole] = useState(localStorage.getItem("role"));
  const [userBookings, setUserBookings] = useState([]);
  const [ownerBookings, setOwnerBookings] = useState([]);
  const [ownerApartments, setOwnerApartments] = useState([]);
  const [allApartments, setAllApartments] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (role === "COMMON_USER" || role === "ADMIN") {
          const { data } = await getBookings();
          setUserBookings(data);
        }
        if (role === "OWNER" || role === "ADMIN") {
          const { data: apartments } = await fetchMyApartments();
          setOwnerApartments(apartments);

          const { data: bookings } = await getBookingRequests();
          setOwnerBookings(bookings);
        }
        if (role === "ADMIN") {
          const { data: apartments } = await fetchAllApartments();
          setAllApartments(apartments);
        }
      } catch (err) {
        console.error("Failed to fetch data:", err);
      }
    };

    fetchData();
  }, [role]);

  const handleDeleteBooking = async (id) => {
    try {
      await deleteBooking(id);
      setUserBookings(userBookings.filter((booking) => booking.id !== id));
      setOwnerBookings(ownerBookings.filter((booking) => booking.id !== id));
      alert("Booking successfully deleted");
    } catch (err) {
      console.error("Failed to delete booking:", err);
      alert("Failed to cancel booking.");
    }
  };

  const handleUpdateBookingStatus = async (id, status) => {
    try {
      await updateBookingStatus(id, status);
      setOwnerBookings(
        ownerBookings.map((booking) =>
          booking.id === id ? { ...booking, status } : booking
        )
      );
      alert(`Booking status updated to ${status}.`);
    } catch (err) {
      console.error("Failed to update booking status:", err);
      alert("Failed to update booking status.");
    }
  };

  const handleDeleteApartment = async (id) => {
    try {
      await deleteApartment(id);
      setOwnerApartments(ownerApartments.filter((apartment) => apartment.id !== id));
      alert("Apartment successfully deleted.");
    } catch (err) {
      console.error("Failed to delete apartment:", err);
      alert("Failed to delete apartment.");
    }
  };

  const handlePublish = async (id, isPublished) => {
    try {
      await publishApartment(id, !isPublished);
      alert(`Apartment ${isPublished ? "unpublished" : "published"} successfully!`);
      const { data: apartments } = await fetchAllApartments();
      setAllApartments(apartments);
    } catch (err) {
      console.error("Failed to update publish status:", err);
      alert("Something went wrong. Please try again.");
    }
  };

  return (
    <div className="profile-page">
      <Header />
      <h1>Profile</h1>
      
      {(role === "COMMON_USER" || role === "ADMIN") && (
        <div>
          <h2>My Bookings</h2>
          {userBookings.length === 0 ? (
            <p>No bookings found.</p>
          ) : (
            <ul>
              {userBookings.map((booking) => (
                <li key={booking.id}>
                  Apartment: {booking.apartmentTitle} <br />
                  From: {booking.startDate} To: {booking.endDate} <br />
                  Status: {booking.status}
                  <button
                    onClick={() => handleDeleteBooking(booking.id)}
                    style={{ marginLeft: "10px", background: "red", color: "white" }}
                  >
                    Cancel Booking
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
      
      {(role === "OWNER" || role === "ADMIN") && (
        <>
          <div>
            <h2>Bookings for Your Apartments</h2>
            {ownerBookings.length === 0 ? (
              <p>No bookings found.</p>
            ) : (
              <ul>
                {ownerBookings.map((booking) => (
                  <li key={booking.id}>
                    Apartment: {booking.apartmentTitle} <br />
                    From: {booking.startDate} To: {booking.endDate} <br />
                    Status: {booking.status} <br />
                    Username: {booking.username} <br />

                    <div className="btn-container">
                    <button
                      onClick={() => handleUpdateBookingStatus(booking.id, "CONFIRMED")}
                      style={{ marginLeft: "10px", background: "green", color: "white" }}
                    >
                      Confirm
                    </button>
                    <button
                      onClick={() => handleUpdateBookingStatus(booking.id, "CANCELLED")}
                      style={{ marginLeft: "10px", background: "red", color: "white" }}
                    >
                      Cancel
                    </button>
                    </div>
                    
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div>
            <h2>Your Apartments</h2>
            <button
              className="add-apartment-btn"
              onClick={() => navigate("/apartments/create")}
            >
              Add Apartment
            </button>
            {ownerApartments.length === 0 ? (
              <p>No apartments found.</p>
            ) : (
              <ul>
                {ownerApartments.map((apartment) => (
                  <li key={apartment.id}>
                    Title: {apartment.title} <br />
                    Description: {apartment.description} <br />
                    <img src={`http://localhost:8090${apartment.photoUrl}`} alt={apartment.title}  onClick={() => navigate(`/apartments/${apartment.id}`)}/> <br />
                    <div className="btn-container">
                      <button
                        onClick={() => navigate(`/apartments/edit/${apartment.id}`)}
                        style={{ marginLeft: "10px", background: "blue", color: "white" }}
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDeleteApartment(apartment.id)}
                        style={{ marginLeft: "10px", background: "red", color: "white" }}
                      >
                        Delete
                      </button>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </>
      )}

      {role === "ADMIN" && (
        <div>
          <h2>All Apartments</h2>
          {allApartments.length === 0 ? (
            <p>No apartments found.</p>
          ) : (
            <ul>
              {allApartments.map((apartment) => (
                <li key={apartment.id}>
                  Title: {apartment.title} <br />
                  Address: {apartment.address} <br />
                  Published: {apartment.published ? "Yes" : "No"} <br />
                  <img src={`http://localhost:8090${apartment.photoUrl}`} alt={apartment.title}  onClick={() => navigate(`/apartments/${apartment.id}`)}/> <br />
                  <button
                    onClick={() => handlePublish(apartment.id, apartment.published)}
                    style={{
                      marginLeft: "10px",
                      background: apartment.published ? "red" : "green",
                      color: "white",
                    }}
                  >
                    {apartment.published ? "Unpublish" : "Publish"}
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
