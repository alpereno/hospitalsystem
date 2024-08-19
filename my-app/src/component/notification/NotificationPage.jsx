import React from 'react';
import { Link } from 'react-router-dom';

function NotificationPage() {
  return (
    <div>
      <Link to="/" style={{ float: 'right', fontSize: '18px', marginRight: '20px' }}>
        Go back to Hospital Page
      </Link>
      <h1>Hello, this is the Notification Page!</h1>
    </div>
  );
}

export default NotificationPage;
