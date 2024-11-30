import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import './style.css'
import Login from './pages/Login'
import IntroPage from './pages/IntroPage'

function App() {

  return (
    <>
      <div>
        <Router>
          <Routes>
            <Route path="/" element={ <IntroPage />} />
            <Route path="/login" element={ <Login />} />
          </Routes>
        </Router>
      </div>
      
    </>
  )
}

export default App
