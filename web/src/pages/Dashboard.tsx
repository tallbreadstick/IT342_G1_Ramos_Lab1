import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile, isAuthenticated } from '../api/auth';
import type { User } from '../api/auth';

export default function Dashboard() {
    const navigate = useNavigate();
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!isAuthenticated()) {
            navigate('/login');
            return;
        }

        const fetchProfile = async () => {
            try {
                const profileData = await getProfile();
                setUser(profileData);
            } catch (err) {
                console.error('Failed to load profile:', err);
                navigate('/login');
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, [navigate]);

    if (loading) {
        return (
            <div className="min-h-[calc(100vh-4rem)] bg-white flex items-center justify-center">
                <div className="text-center">
                    <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"></div>
                    <p className="mt-4 text-gray-700">loading...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-[calc(100vh-4rem)] bg-white">
            <div className="max-w-screen-2xl mx-auto p-8">
                <div className="mb-8">
                    <h1 className="text-3xl font-bold text-gray-900 mb-1">{user?.username}</h1>
                    <p className="text-gray-600">{user?.email}</p>
                </div>

                <div className="content-card">
                    <div className="border-t border-gray-200 pt-6">
                        <table className="w-full text-left">
                            <tbody className="divide-y divide-gray-200">
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500 w-48">user id</td>
                                    <td className="py-3 text-sm text-gray-900">{user?.id}</td>
                                </tr>
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500">username</td>
                                    <td className="py-3 text-sm text-gray-900">{user?.username}</td>
                                </tr>
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500">email</td>
                                    <td className="py-3 text-sm text-gray-900">{user?.email}</td>
                                </tr>
                                <tr>
                                    <td className="py-3 text-sm font-medium text-gray-500">joined</td>
                                    <td className="py-3 text-sm text-gray-900">
                                        {user?.dateJoined ? new Date(user.dateJoined).toLocaleDateString() : 'n/a'}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>
        </div>
    );
}
