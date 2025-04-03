import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';
import Swal from 'sweetalert2';

function SignupPage() {
    const [fullName, setFullName] = useState('');
    const [username, setUsername] = useState('');
    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('etudiant');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSignup = async () => {
        try {
            if (!fullName || !mail || !password || !confirmPassword) {
                // Alerte pour les champs manquants
                Swal.fire({
                    icon: 'warning',
                    title: 'Champs manquants',
                    text: 'Veuillez remplir tous les champs.',
                });
                return;
            }

            if (password !== confirmPassword) {
                // Alerte pour mots de passe non correspondants
                Swal.fire({
                    icon: 'error',
                    title: 'Erreur',
                    text: 'Les mots de passe ne correspondent pas.',
                });
                return;
            }

            const response = await axios.post(
                'http://localhost:8080/auth/signup',
                {
                    fullName,
                    username,
                    mail,
                    password,
                    role,
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );

            console.log(response.data);

            // Stocker les informations importantes dans le localStorage
            localStorage.setItem('role', 'etudiant'); // Stocker le rôle (par exemple "etudiant" ou "admin")
            localStorage.setItem('token', response.data.jwt); // Stocker le token d'authentification
            localStorage.setItem('idUser', response.data.idUser); // Stocker l'identifiant utilisateur

            // Alerte de succès
            Swal.fire({
                icon: 'success',
                title: 'Inscription réussie',
                text: 'Votre compte a été créé avec succès !',
                timer: 2000, // Disparaît après 2 secondes
                showConfirmButton: false,
            });

            // Rediriger après la sauvegarde
            navigate('/Room');
        } catch (error) {
            // Alerte pour échec d'inscription
            Swal.fire({
                icon: 'error',
                title: 'Inscription échouée',
                text: 'L\'inscription a échoué. Veuillez réessayer.',
            });
            console.error('Signup failed:', error.response ? error.response.data : error.message);
        }
    };


    const handleLoginClick = () => {
        navigate('/');
    };

    return (
        <div className="login-container">
            <div className="login-box">
                {/* Logo et Titre */}
                <div className="header-section">
                    <div className="logo-wrapper">
                        <img src="/logo-white.png" alt="Logo Master ISI" className="login-logo" />
                    </div>
                    <h1 className="login-title">Créer un compte</h1>
                    <p className="login-subtitle">Rejoignez Master ISI Chat</p>
                </div>

                {/* Formulaire */}
                <div className="form-section">
                    {error && <div className="error-message">{error}</div>}

                    <div className="form-group">
                        <label>Nom complet</label>
                        <input
                            type="text"
                            placeholder="Entrez votre nom complet"
                            value={fullName}
                            onChange={(e) => setFullName(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Nom d'utilisateur</label>
                        <input
                            type="text"
                            placeholder="Choisissez un nom d'utilisateur"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            placeholder="Entrez votre email"
                            value={mail}
                            onChange={(e) => setMail(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Mot de passe</label>
                        <input
                            type="password"
                            placeholder="Créez votre mot de passe"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Confirmer le mot de passe</label>
                        <input
                            type="password"
                            placeholder="Confirmez votre mot de passe"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                        />
                    </div>

                    <button className="login-btn" onClick={handleSignup}>
                        S'inscrire
                    </button>
                </div>

                {/* Footer */}
                <div className="footer-section">
                    <p>
                        Déjà inscrit ?{' '}
                        <button onClick={handleLoginClick} className="signup-link">
                            Se connecter
                        </button>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default SignupPage;