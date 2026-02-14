import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../api/auth';
import type { RegisterRequest } from '../api/auth';

export default function Register() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState<RegisterRequest>({
        username: '',
        email: '',
        password: '',
    });

    const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setMessage(null);

        try {
            // Basic validation
            if (!formData.username || !formData.email || !formData.password) {
                setMessage({ type: 'error', text: 'All fields are required' });
                setLoading(false);
                return;
            }

            if (formData.password.length < 6) {
                setMessage({ type: 'error', text: 'Password must be at least 6 characters' });
                setLoading(false);
                return;
            }

            await register(formData);
            setMessage({
                type: 'success',
                text: 'Registration successful! You can now log in.',
            });
            setFormData({ username: '', email: '', password: '' });
        } catch (error: unknown) {
            const errorMessage = error instanceof Error ? error.message : 'Registration failed';
            setMessage({ type: 'error', text: errorMessage });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-[calc(100vh-4rem)] bg-white flex items-center justify-center p-4">
            <div className="w-full max-w-md auth-card">
                <h1 className="text-2xl font-bold text-gray-900 mb-6">register</h1>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="username" className="block text-sm text-gray-700 mb-1">
                            username
                        </label>
                        <input
                            id="username"
                            name="username"
                            type="text"
                            value={formData.username}
                            onChange={handleChange}
                            className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-gray-900"
                        />
                    </div>

                    <div>
                        <label htmlFor="email" className="block text-sm text-gray-700 mb-1">
                            email
                        </label>
                        <input
                            id="email"
                            name="email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-gray-900"
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm text-gray-700 mb-1">
                            password
                        </label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                            className="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-gray-900"
                        />
                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full px-4 py-2 text-sm bg-gray-900 text-white hover:bg-gray-800 transition disabled:opacity-50"
                    >
                        {loading ? 'registering...' : 'register'}
                    </button>
                </form>

                {message && (
                    <div
                        className={`mt-4 p-3 border ${message.type === 'success'
                                ? 'border-green-300 bg-green-50'
                                : 'border-red-300 bg-red-50'
                            }`}
                    >
                        <p
                            className={`text-sm ${message.type === 'success' ? 'text-green-700' : 'text-red-700'
                                }`}
                        >
                            {message.text}
                        </p>
                    </div>
                )}

                <div className="mt-6 text-sm text-gray-600">
                    already registered?{' '}
                    <button
                        onClick={() => navigate('/login')}
                        className="text-gray-900 underline hover:no-underline"
                    >
                        login
                    </button>
                </div>
            </div>
        </div>
    );
}
