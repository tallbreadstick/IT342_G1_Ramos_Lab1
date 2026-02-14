import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Register from './pages/Register';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import './index.css';

function Home() {
    return (
        <div className="min-h-[calc(100vh-4rem)] bg-white flex items-center justify-center p-8">
            <div className="text-center">
                <h1 className="text-4xl font-bold text-gray-900 mb-2">auth system</h1>
                <p className="text-gray-600">login or register to continue</p>
            </div>
        </div>
    );
}

export default function App() {
    return (
        <Router>
            <div className="min-h-screen bg-white">
                <Navbar />
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/profile" element={<Navigate to="/dashboard" />} />
                    <Route path="*" element={<Navigate to="/" />} />
                </Routes>
            </div>
        </Router>
    );
}