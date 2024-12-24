import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { fetchApartment, saveApartment, uploadPhoto } from "../api";
import "../styles/ApartmentFormPage.css";

const ApartmentFormPage = () => {
  const { apartmentId } = useParams();
  const navigate = useNavigate();
  const [apartment, setApartment] = useState({
    title: "",
    address: "",
    description: "",
    pricePerMonth: "",
    photos: "",
  });

  const [selectedFile, setSelectedFile] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (apartmentId) {
      const fetchData = async () => {
        try {
          const { data } = await fetchApartment(apartmentId);
          setApartment(data);
        } catch (err) {
          console.error("Failed to load apartment:", err);
          setError("Failed to load apartment data.");
        }
      };
      fetchData();
    }
  }, [apartmentId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setApartment((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  const uploadPhotoAndGetUrl = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    try {
      const { data } = await uploadPhoto(formData);
      return data;
    } catch (error) {
      console.error("Error uploading photo:", error);
      setError("Failed to upload photo.");
      throw error;
    }
  };

  // Обработчик отправки формы
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (selectedFile) {
        const photoUrl = await uploadPhotoAndGetUrl(selectedFile);
        
        const updatedApartment = {
          ...apartment,
          photoUrl: photoUrl,
        };

        await saveApartment(updatedApartment);
      }

      alert(apartmentId ? "Apartment updated successfully!" : "Apartment added successfully!");
      navigate("/profile");
    } catch (err) {
      console.error("Failed to save apartment:", err);
      setError("Failed to save apartment. Please try again.");
    }
  };

  return (
    <div className="apartment-form-page">
      <h1>{apartmentId ? "Edit Apartment" : "Add Apartment"}</h1>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit} className="apartment-form">
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={apartment.title}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="address">Address</label>
          <input
            type="text"
            id="address"
            name="address"
            value={apartment.address}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={apartment.description}
            onChange={handleChange}
            required
          ></textarea>
        </div>
        <div className="form-group">
          <label htmlFor="pricePerMonth">Price Per Month</label>
          <input
            type="number"
            id="pricePerMonth"
            name="pricePerMonth"
            value={apartment.pricePerMonth}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="photos">Upload Photo</label>
          <input
            type="file"
            id="photos"
            name="photos"
            onChange={handleFileChange}
          />
        </div>
        <button type="submit" className="submit-button">
          {apartmentId ? "Save Changes" : "Add Apartment"}
        </button>
      </form>
    </div>
  );
};

export default ApartmentFormPage;
