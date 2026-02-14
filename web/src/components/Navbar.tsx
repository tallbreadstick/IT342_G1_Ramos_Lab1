import { Link, useNavigate, useLocation } from 'react-router-dom';
import { isAuthenticated, logout, logoutServer } from '../api/auth';

export default function Navbar() {
    const navigate = useNavigate();
    const location = useLocation();
    const authenticated = isAuthenticated();

    const handleLogout = async () => {
        try {
            await logoutServer();
        } catch (err) {
            console.error('Logout failed:', err);
        } finally {
            logout();
            navigate('/login');
        }
    };

    const isActive = (path: string) => {
        return location.pathname === path;
    };

    return (
        <nav className="site-nav">
            <div className="max-w-screen-2xl" style={{width:'100%',display:'flex',alignItems:'center',justifyContent:'space-between'}}>
                <Link to="/" className="brand">
                    auth
                </Link>

                <div className="nav-links">
                    {authenticated ? (
                        <>
                            <Link to="/dashboard" className={`nav-link ${isActive('/dashboard') ? 'active' : ''}`}>
                                dashboard
                            </Link>
                            <button onClick={handleLogout} className="nav-link">
                                logout
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/login" className={`nav-link ${isActive('/login') ? 'active' : ''}`}>
                                login
                            </Link>
                            <Link to="/register" className={`nav-link ${isActive('/register') ? 'active' : ''}`}>
                                register
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
}
