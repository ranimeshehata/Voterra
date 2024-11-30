import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import './style.css'
import Login from './pages/Login'
import HomePage from './pages/HomePage'
import IntroPage from './pages/IntroPage'
import { UserProvider } from './context/UserContext'

function App() {

  return (
    <UserProvider>
      <div>
        <Router>
          <Routes>
            <Route path="/" element={ <IntroPage />} />
            <Route path="/login" element={ <Login />} />
            <Route path="/homepage" element={ <HomePage />} />
          </Routes>
        </Router>
      </div>
      
    </UserProvider>
  )
}

export default App
