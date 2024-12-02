import { useContext } from 'react';
import { UserContext } from '../context/UserContext';

function HomePage() {

  const { user } = useContext(UserContext);

    return (
      <div>
        <h1>Home Page</h1>
        <p> Welcome {user.username}</p>
      </div>
    );
  }

    export default HomePage;