import React, { useEffect, useState } from "react";
import { fetchApartment, checkAvailability, createBooking } from "../api/index.js";
import { useParams } from "react-router-dom";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import Header from "../components/Header.jsx";
import "../styles/ApartmentDetailPage.css"

const ApartmentDetailPage = () => {
  const { id } = useParams();
  const [apartment, setApartment] = useState(null);
  const [availableDates, setAvailableDates] = useState([]);
  const [selectedRange, setSelectedRange] = useState(null);
  const [currentYear, setCurrentYear] = useState(new Date().getFullYear());

  useEffect(() => {
    const loadApartment = async () => {
      try {
        const { data } = await fetchApartment(id);
        setApartment(data);
      } catch (error) {
        console.error("Error fetching apartment details:", error);
      }
    };

    loadApartment();
  }, [id]);

  useEffect(() => {
    const loadAvailability = async () => {
      try {
        const startDay = `${currentYear}-01-01`;
        const endDay = `${currentYear}-12-31`;
        const { data } = await checkAvailability(id, startDay, endDay);
        setAvailableDates(data);
      } catch (error) {
        console.error("Error fetching availability:", error);
      }
    };

    loadAvailability();
  }, [id, currentYear]);

  const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const handleBooking = async () => {
    if (!selectedRange) {
      alert("Please select a date range before booking.");
      return;
    }

    const [startDate, endDate] = selectedRange.map(formatDate);

    const isRangeAvailable = ([start, end]) => {
      if (!start || !end) return false;

      const current = new Date(start);
      const endDate = new Date(end);

      while (current <= endDate) {
        const formattedDate = formatDate(current);
        console.log(formattedDate);
        if (!availableDates.includes(formattedDate)) {
          return false;
        }
        current.setDate(current.getDate() + 1);
      }
      return true;
    };

    if (!isRangeAvailable([new Date(startDate), new Date(endDate)])) {
      alert("Selected range includes unavailable dates. Please select another range.");
      return;
    }

    try {
      await createBooking({
        apartmentId: id,
        startDate,
        endDate,
      });
      alert("Booking successful!");
    } catch (error) {
      console.error("Error creating booking:", error);
      alert("Something went wrong. Please try again later.");
    }
  };

  const tileClassName = ({ date, view }) => {
    if (view === "month") {
      const formattedDate = formatDate(date);
      if (availableDates.includes(formattedDate)) {
        return "react-calendar__tile--available";
      }
    }
    return null;
  };

  const handleYearChange = (date) => {
    const newYear = date.getFullYear();
    if (newYear !== currentYear) {
      setCurrentYear(newYear);
    }
  };

  if (!apartment) return <p>Loading...</p>;

  return (
    <div className="home-container">
    <Header />
    <div className="apartment-detail">
      <div className="apartment-slider">
        <img
          src={`http://localhost:8090${apartment.photoUrl}`}
          alt={apartment.title}
        />
      </div>
      <div className="apartment-info-det">
        <h2>{apartment.title}</h2>
        <p>Адрес: {apartment.address}</p>
        <p className="details-extra">{apartment.description}</p>
        <p className="price">{apartment.pricePerMonth} $/month</p>
      </div>

      <div className="calendar-section">
        <h2>Select Available Dates</h2>
        <Calendar
          tileClassName={tileClassName}
          selectRange
          onChange={setSelectedRange}
          onActiveStartDateChange={({ activeStartDate }) => handleYearChange(activeStartDate)}
        />

        {selectedRange && (
          <p>
            Selected range: {selectedRange[0].toLocaleDateString()} -{" "}
            {selectedRange[1].toLocaleDateString()}
          </p>
        )}
      </div>

      <button onClick={handleBooking} className="book-button">
        Book Now
      </button>
    </div>
    </div>
  );
};

export default ApartmentDetailPage;
