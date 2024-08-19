import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function NotificationPage() {
  const [notifications, setNotifications] = useState([]);
  const [targetPatients, setTargetPatients] = useState([]);
  const [newNotification, setNewNotification] = useState({
    notificationMessage: {
      title: '',
      message: '',
    },
    notificationCriteria: {
      startAge: '',
      endAge: '',
      gender: '',
    },
  });

  useEffect(() => {
    fetch('http://localhost:8081/api/notification/getall')
      .then((response) => response.json())
      .then((data) => setNotifications(data))
      .catch((error) => console.error('Error fetching notifications:', error));

    fetch('http://localhost:8081/api/notification/getAllTargetPatient')
      .then((response) => response.json())
      .then((data) => setTargetPatients(data))
      .catch((error) => console.error('Error fetching target patients:', error));
  }, []);

  const handleAddNotification = () => {
    const { title, message } = newNotification.notificationMessage;
    const { startAge, endAge, gender } = newNotification.notificationCriteria;

    if (!title || !message || !startAge || !endAge || !gender) {
      alert('Please fill out all fields.');
      return;
    }

    fetch('http://localhost:8081/api/notification/addNotification', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newNotification),
    })
      .then((response) => response.json())
      .then((data) => {
        setNotifications([...notifications, data]);
        setNewNotification({
          notificationMessage: { title: '', message: '' },
          notificationCriteria: { startAge: '', endAge: '', gender: '' },
        });
        window.location.reload(); 
      })
      .catch((error) => console.error('Error adding notification:', error));
  };

  const handleDeleteNotification = (id) => {
    fetch(`http://localhost:8081/api/notification/delete/${id}`, {
      method: 'DELETE',
    })
      .then(() => {
        setNotifications(notifications.filter((notification) => notification.id !== id));
        window.location.reload(); 
      })
      .catch((error) => console.error('Error deleting notification:', error));
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <Link to="/" style={{ float: 'right', fontSize: '18px', marginRight: '20px', color: '#007bff', textDecoration: 'none' }}>
        Go back to Hospital Page
      </Link>
      <h1 style={{ color: '#333' }}>Hello, this is the Notification Page!</h1>

      <h2 style={{ color: '#555' }}>Add New Notification</h2>
      <div style={{ marginBottom: '20px', display: 'flex', flexWrap: 'wrap', gap: '10px', alignItems: 'center' }}>
        <label style={{ flex: '1 1 200px' }}>
          Title:
          <input
            type="text"
            value={newNotification.notificationMessage.title}
            onChange={(e) =>
              setNewNotification({
                ...newNotification,
                notificationMessage: { ...newNotification.notificationMessage, title: e.target.value },
              })
            }
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </label>
        <label style={{ flex: '1 1 200px' }}>
          Message:
          <input
            type="text"
            value={newNotification.notificationMessage.message}
            onChange={(e) =>
              setNewNotification({
                ...newNotification,
                notificationMessage: { ...newNotification.notificationMessage, message: e.target.value },
              })
            }
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </label>
        <label style={{ flex: '1 1 150px' }}>
          Start Age:
          <input
            type="number"
            value={newNotification.notificationCriteria.startAge}
            onChange={(e) =>
              setNewNotification({
                ...newNotification,
                notificationCriteria: { ...newNotification.notificationCriteria, startAge: e.target.value },
              })
            }
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </label>
        <label style={{ flex: '1 1 150px' }}>
          End Age:
          <input
            type="number"
            value={newNotification.notificationCriteria.endAge}
            onChange={(e) =>
              setNewNotification({
                ...newNotification,
                notificationCriteria: { ...newNotification.notificationCriteria, endAge: e.target.value },
              })
            }
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </label>
        <label style={{ flex: '1 1 150px' }}>
          Gender:
          <input
            type="text"
            value={newNotification.notificationCriteria.gender}
            onChange={(e) =>
              setNewNotification({
                ...newNotification,
                notificationCriteria: { ...newNotification.notificationCriteria, gender: e.target.value },
              })
            }
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </label>
        <button
          onClick={handleAddNotification}
          style={{
            padding: '10px 20px',
            backgroundColor: '#28a745',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            fontWeight: 'bold',
            flex: '1 1 auto',
          }}
        >
          Add Notification
        </button>
      </div>

      <h2 style={{ color: '#555' }}>Notifications</h2>
      <table style={{ width: '100%', textAlign: 'left', marginTop: '20px', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ backgroundColor: '#f2f2f2' }}>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>ID</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Title</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Message</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Start Age</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>End Age</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Gender</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {notifications.map((notification) => (
            <tr key={notification.id}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{notification.id}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{notification.notificationMessage.title}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{notification.notificationMessage.message}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{notification.notificationCriteria.startAge}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{notification.notificationCriteria.endAge}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{notification.notificationCriteria.gender}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                <button
                  onClick={() => handleDeleteNotification(notification.id)}
                  style={{
                    backgroundColor: '#dc3545',
                    color: 'white',
                    border: 'none',
                    borderRadius: '4px',
                    padding: '5px 10px',
                    cursor: 'pointer',
                  }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <h2 style={{ color: '#555' }}>Target Patients</h2>
      <table style={{ width: '100%', textAlign: 'left', marginTop: '20px', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ backgroundColor: '#f2f2f2' }}>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Patient ID</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Notification ID</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Primary Phone</th>
            <th style={{ border: '1px solid #ddd', padding: '8px' }}>Primary Mail</th>
          </tr>
        </thead>
        <tbody>
          {targetPatients.map((patient) => (
            <tr key={patient.patientId}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{patient.patientId}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{patient.notificationId}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{patient.primaryPhone}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{patient.primaryMail}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default NotificationPage;
