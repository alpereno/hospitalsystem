import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const HospitalPage = () => {
  const [patients, setPatients] = useState([]);
  const [newPatient, setNewPatient] = useState({
    firstName: '',
    middleName: '',
    lastName: '',
    dateOfBirth: '',
    gender: '',
    address: '',
    tckn: '',
    passportNumber: '',
    isSmsActive: '',
    isEmailActive: '',
    phoneNumbers: [],
    emailAddresses: []
  });
  const [errors, setErrors] = useState({});
  const [popupMessage, setPopupMessage] = useState('');
  const [searchQuery, setSearchQuery] = useState(''); // Added state for search query

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/patient/getall');
      setPatients(response.data);
    } catch (error) {
      console.error('Error fetching patients:', error);
    }
  };
  

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === 'tckn' && isNaN(value)) return;
    setNewPatient(prevState => ({ ...prevState, [name]: value }));
  };

  const handlePhoneNumberChange = (index, e) => {
    const { name, value } = e.target;
    const updatedPhoneNumbers = [...newPatient.phoneNumbers];
    updatedPhoneNumbers[index][name] = value;
    setNewPatient(prevState => ({ ...prevState, phoneNumbers: updatedPhoneNumbers }));
  };

  const handleEmailAddressChange = (index, e) => {
    const { name, value } = e.target;
    const updatedEmailAddresses = [...newPatient.emailAddresses];
    updatedEmailAddresses[index][name] = value;
    setNewPatient(prevState => ({ ...prevState, emailAddresses: updatedEmailAddresses }));
  };

  const handleCheckboxChange = (e) => {
    const { name, checked } = e.target;
    setNewPatient(prevState => ({ ...prevState, [name]: checked }));
  };

  const validateFields = () => {
    let tempErrors = {};
    if (!newPatient.firstName) tempErrors.firstName = 'First Name is required';
    if (!newPatient.lastName) tempErrors.lastName = 'Last Name is required';
    if (!newPatient.dateOfBirth) tempErrors.dateOfBirth = 'Date of Birth is required';
    if (!newPatient.gender || newPatient.gender.length !== 1) tempErrors.gender = 'Gender must be 1 character';
    if (!newPatient.address) tempErrors.address = 'Address is required';
    if (!newPatient.tckn || newPatient.tckn.length !== 11) tempErrors.tckn = 'TCKN must be 11 digits';
    if (!newPatient.passportNumber) tempErrors.passportNumber = 'Passport Number is required';
    setErrors(tempErrors);

    return Object.keys(tempErrors).length === 0;
  };

  const validateFieldsForUpdate = () => {
    let tempErrors = {};
    if (!newPatient.firstName) tempErrors.firstName = 'First Name is required';
    if (!newPatient.lastName) tempErrors.lastName = 'Last Name is required';
    if (!newPatient.gender || newPatient.gender.length !== 1) tempErrors.gender = 'Gender must be 1 character';
    if (!newPatient.address) tempErrors.address = 'Address is required';
    setErrors(tempErrors);

    return Object.keys(tempErrors).length === 0;
  };

  const handleAddPatient = async () => {
    if (!validateFields()) {
      setPopupMessage('Please correct the errors before submitting.');
      setTimeout(() => setPopupMessage(''), 3000);
      return;
    }

    try {
      const response = await axios.post('http://localhost:8080/api/patient/addPatient', newPatient);
      setPatients([...patients, response.data]);
      setNewPatient({
        firstName: '',
        middleName: '',
        lastName: '',
        dateOfBirth: '',
        gender: '',
        address: '',
        tckn: '',
        passportNumber: '',
        isSmsActive: '',
        isEmailActive: '',
        phoneNumbers: [],
        emailAddresses: []
      });
      setErrors({});
      setPopupMessage('Patient added successfully!');
      setTimeout(() => setPopupMessage(''), 3000);
    } catch (error) {
      console.error('Error adding patient:', error);
      setPopupMessage('An error occurred while adding the patient.');
      setTimeout(() => setPopupMessage(''), 3000);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/patient/delete/${id}`);
      setPatients(patients.filter(patient => patient.id !== id));
      setPopupMessage('Patient deleted successfully!');
      setTimeout(() => setPopupMessage(''), 3000);
    } catch (error) {
      console.error('Error deleting patient:', error);
      setPopupMessage('An error occurred while deleting the patient.');
      setTimeout(() => setPopupMessage(''), 3000);
    }
  };

  const handleUpdatePatient = async (id) => {
    if (!validateFieldsForUpdate()) {
      setPopupMessage('Please correct the errors before submitting.');
      setTimeout(() => setPopupMessage(''), 3000);
      return;
    }
  
    try {
      // URL şablonunu doğru bir şekilde kullanmak için backtick işareti kullanın
      const response = await axios.put(`http://localhost:8080/api/patient/update/${id}`, newPatient);
      
      // Güncellenmiş hastayı bulup, hastalar listesini güncelleyin
      setPatients(patients.map(patient => patient.id === id ? response.data : patient));
  
      // Formu sıfırla
      setNewPatient({
        firstName: '',
        middleName: '',
        lastName: '',
        dateOfBirth: '',
        gender: '',
        address: '',
        tckn: '',
        passportNumber: '',
        isSmsActive: '',
        isEmailActive: '',
        phoneNumbers: [],
        emailAddresses: []
      });
      setErrors({});
      setPopupMessage('Patient updated successfully!');
      setTimeout(() => setPopupMessage(''), 3000);
    } catch (error) {
      console.error('Error updating patient:', error);
      setPopupMessage('An error occurred while updating the patient.');
      setTimeout(() => setPopupMessage(''), 3000);
    }
  };

  const searchPatientsByName = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/patient/searchName/${searchQuery}`);
      setPatients(response.data);
    } catch (error) {
      console.error('Error searching patients:', error);
    }
  };
  

  const addPhoneNumberField = () => {
    if (newPatient.phoneNumbers.length < 3) {
      setNewPatient(prevState => ({
        ...prevState,
        phoneNumbers: [...prevState.phoneNumbers, { phoneNumber: '', phoneType: 'mobile' }]
      }));
    }
  };

  const removePhoneNumberField = (index) => {
    setNewPatient(prevState => ({
      ...prevState,
      phoneNumbers: prevState.phoneNumbers.filter((_, i) => i !== index)
    }));
  };

  const addEmailAddressField = () => {
    if (newPatient.emailAddresses.length < 3) {
      setNewPatient(prevState => ({
        ...prevState,
        emailAddresses: [...prevState.emailAddresses, { emailAddress: '', emailType: 'Personal' }]
      }));
    }
  };

  const removeEmailAddressField = (index) => {
    setNewPatient(prevState => ({
      ...prevState,
      emailAddresses: prevState.emailAddresses.filter((_, i) => i !== index)
    }));
  };

  return (
    <div style={{ textAlign: 'center', margin: '0 auto', width: '80%' }}>
      {/* Popup Message */}
      {popupMessage && (
        <div style={{
          position: 'fixed',
          top: '10px',
          right: '10px',
          backgroundColor: '#f8d7da',
          color: '#721c24',
          border: '1px solid #f5c6cb',
          borderRadius: '5px',
          padding: '10px',
          zIndex: 1000
        }}>
          {popupMessage}
        </div>
      )}

      <Link to="/notifications" style={{ float: 'right', fontSize: '18px', marginRight: '20px', textDecoration: 'none', color: '#007bff' }}>
        Go to Notification Page
      </Link>

      <h1 style={{ marginBottom: '20px', fontSize: '36px' }}>Hospital Patient Management</h1>

      {/* Add Patient Form */}
      <div style={{ marginBottom: '40px', padding: '20px', border: '1px solid #ccc', borderRadius: '10px', backgroundColor: '#f9f9f9' }}>
        <h2 style={{ marginBottom: '20px' }}>Add New Patient</h2>
        <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: '10px' }}>
          <input
            type="text"
            name="firstName"
            placeholder="First Name"
            value={newPatient.firstName}
            onChange={handleInputChange}
            style={{ borderColor: errors.firstName ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="text"
            name="middleName"
            placeholder="Middle Name"
            value={newPatient.middleName}
            onChange={handleInputChange}
            style={{ padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="text"
            name="lastName"
            placeholder="Last Name"
            value={newPatient.lastName}
            onChange={handleInputChange}
            style={{ borderColor: errors.lastName ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="date"
            name="dateOfBirth"
            value={newPatient.dateOfBirth}
            onChange={handleInputChange}
            style={{ borderColor: errors.dateOfBirth ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="text"
            name="gender"
            placeholder="Gender"
            value={newPatient.gender}
            onChange={handleInputChange}
            style={{ borderColor: errors.gender ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="text"
            name="address"
            placeholder="Address"
            value={newPatient.address}
            onChange={handleInputChange}
            style={{ borderColor: errors.address ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="text"
            name="tckn"
            placeholder="TCKN"
            value={newPatient.tckn}
            onChange={handleInputChange}
            style={{ borderColor: errors.tckn ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
          <input
            type="text"
            name="passportNumber"
            placeholder="Passport Number"
            value={newPatient.passportNumber}
            onChange={handleInputChange}
            style={{ borderColor: errors.passportNumber ? 'red' : '#ccc', padding: '10px', borderRadius: '5px', width: '220px' }}
          />
        </div>

        {/* Phone Numbers */}
        {newPatient.phoneNumbers.map((phoneNumber, index) => (
          <div key={index} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', margin: '10px 0' }}>
            <input
              type="text"
              name="phoneNumber"
              placeholder="Phone Number"
              value={phoneNumber.phoneNumber}
              onChange={(e) => handlePhoneNumberChange(index, e)}
              style={{ padding: '10px', borderRadius: '5px', width: '220px', marginRight: '10px' }}
            />
            <select
              name="phoneType"
              value={phoneNumber.phoneType}
              onChange={(e) => handlePhoneNumberChange(index, e)}
              style={{ padding: '10px', borderRadius: '5px', marginRight: '10px' }}
            >
              <option value="mobile">Mobile</option>
              <option value="work">Work</option>
              <option value="home">Home</option>
            </select>
            <button onClick={() => removePhoneNumberField(index)} style={{ padding: '10px 20px', borderRadius: '5px', backgroundColor: '#dc3545', color: 'white', border: 'none' }}>
              Remove
            </button>
          </div>
        ))}
        {newPatient.phoneNumbers.length < 3 && (
          <button onClick={addPhoneNumberField} style={{ padding: '10px 20px', borderRadius: '5px', backgroundColor: '#28a745', color: 'white', border: 'none', marginTop: '10px' }}>
            Add Phone Number
          </button>
        )}

        {/* Email Addresses */}
        {newPatient.emailAddresses.map((emailAddress, index) => (
          <div key={index} style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', margin: '10px 0' }}>
            <input
              type="text"
              name="emailAddress"
              placeholder="Email Address"
              value={emailAddress.emailAddress}
              onChange={(e) => handleEmailAddressChange(index, e)}
              style={{ padding: '10px', borderRadius: '5px', width: '220px', marginRight: '10px' }}
            />
            <select
              name="emailType"
              value={emailAddress.emailType}
              onChange={(e) => handleEmailAddressChange(index, e)}
              style={{ padding: '10px', borderRadius: '5px', marginRight: '10px' }}
            >
              <option value="Personal">Personal</option>
              <option value="Work">Work</option>
            </select>
            <button onClick={() => removeEmailAddressField(index)} style={{ padding: '10px 20px', borderRadius: '5px', backgroundColor: '#dc3545', color: 'white', border: 'none' }}>
              Remove
            </button>
          </div>
        ))}
        {newPatient.emailAddresses.length < 3 && (
          <button onClick={addEmailAddressField} style={{ padding: '10px 20px', borderRadius: '5px', backgroundColor: '#28a745', color: 'white', border: 'none', marginTop: '10px' }}>
            Add Email Address
          </button>
        )}

        <div style={{ marginTop: '20px' }}>
          <label style={{ marginRight: '20px' }}>
            <input
              type="checkbox"
              name="smsActive"
              checked={newPatient.smsActive}
              onChange={handleCheckboxChange}
              style={{ marginRight: '10px' }}
            />
            SMS Active
          </label>
          <label>
            <input
              type="checkbox"
              name="emailActive"
              checked={newPatient.emailActive}
              onChange={handleCheckboxChange}
              style={{ marginRight: '10px' }}
            />
            Email Active
          </label>
        </div>

        <button onClick={handleAddPatient} style={{ padding: '10px 20px', borderRadius: '5px', backgroundColor: '#007bff', color: 'white', border: 'none', marginTop: '20px' }}>
          Add Patient
        </button>
      </div>
            {/* Search Bar */}
            <div style={{ marginBottom: '20px' }}>
        <input
          type="text"
          placeholder="Search by name"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          style={{ padding: '10px', borderRadius: '5px', width: '300px', marginRight: '10px' }}
        />
        <button onClick={searchPatientsByName} style={{ padding: '10px 20px', borderRadius: '5px', backgroundColor: '#007bff', color: 'white', border: 'none' }}>
          Search
        </button>
      </div>

      {/* Patients Table */}
      <div>
        <h2>Patients List</h2>
        <table style={{
  width: '100%',
  borderCollapse: 'collapse',
  textAlign: 'center',
  marginBottom: '20px',
  boxShadow: '0px 0px 15px rgba(0, 0, 0, 0.1)',
  borderRadius: '10px',
  overflow: 'hidden'
}}>
  <thead>
    <tr style={{ backgroundColor: '#007bff', color: 'white' }}>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>ID</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>First Name</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Middle Name</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Last Name</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Date of Birth</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Gender</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Address</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>TCKN</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Passport Number</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Phone Numbers</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Email Addresses</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>SMS Active</th>
      <th style={{ padding: '15px', borderRight: '1px solid white' }}>Email Active</th>
      <th style={{ padding: '15px' }}>Actions</th>
    </tr>
  </thead>
  <tbody>
    {patients.map(patient => (
      <tr key={patient.id} style={{
        backgroundColor: '#f9f9f9',
        transition: 'background-color 0.3s ease',
        borderBottom: '1px solid #ddd'
      }}
      onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#f1f1f1'}
      onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#f9f9f9'}>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.id}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.firstName}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.middleName}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.lastName}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.dateOfBirth}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.gender}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.address}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.tckn}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>{patient.passportNumber}</td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>
          {patient.phoneNumbers.map((phone, index) => (
            <div key={index}>{phone.phoneNumber} ({phone.phoneType})</div>
          ))}
        </td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>
          {patient.emailAddresses.map((email, index) => (
            <div key={index}>{email.emailAddress} ({email.emailType})</div>
          ))}
        </td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>
          {patient.smsActive ? 'Yes' : 'No'}
        </td>
        <td style={{ padding: '15px', borderRight: '1px solid #ddd' }}>
          {patient.emailActive ? 'Yes' : 'No'}
        </td>
        <td style={{ padding: '15px' }}>
          <button onClick={() => handleDelete(patient.id)} style={{
            padding: '8px 15px',
            borderRadius: '5px',
            backgroundColor: '#dc3545',
            color: 'white',
            border: 'none'
          }}>
            Delete
          </button>
        </td>
        <td style={{ padding: '15px' }}>
          <button onClick={() => handleUpdatePatient(patient.id)} style={{
            padding: '8px 15px',
            borderRadius: '5px',
            backgroundColor: '#007bff',
            color: 'white',
            border: 'none'
          }}>
            Update
          </button>
        </td>
      </tr>
    ))}
  </tbody>
</table>

      </div>
    </div>
  );
};

export default HospitalPage;
