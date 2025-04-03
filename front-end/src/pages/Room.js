import React, {useState, useEffect, useCallback} from "react";
import axios from "axios";
import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {FaPaperPlane} from "react-icons/fa";
import "../styles/Room.css";
import {Button} from "@material-tailwind/react";
import Swal from "sweetalert2";
import {useNavigate} from "react-router-dom";


let stompClient = null;

function Room() {
    const [rooms, setRooms] = useState([]); // Liste des rooms
    const [imageUrls, setImageUrls] = useState({}); // URLs des images de profil
    const [messages, setMessages] = useState([]); // Messages de la Room active
    const [messageContent, setMessageContent] = useState(""); // Message à envoyer
    const [currentRoom, setCurrentRoom] = useState(null); // Room sélectionnée
    const [showModal, setShowModal] = useState(false); // Affichage modal
    const [roomDetails, setRoomDetails] = useState(null); // Détails de la room
    const [open, setOpen] = useState(false); // Contrôle du modal
    const [selectedRoom, setSelectedRoom] = useState(null); // Room sélectionnée
    const [openAddModal, setOpenAddModal] = useState(false);
    const [newRoom, setNewRoom] = useState({titre: "", description: "", profile: null});
    const [isEditing, setIsEditing] = useState(false); // État pour le mode édition
    const [editableRoom, setEditableRoom] = useState(null); // Données modifiables de la room
    const [userList, setUserList] = useState([]); // Liste des utilisateurs d'une room
    const [openUserModal, setOpenUserModal] = useState(false); // Contrôle de la modal des utilisateurs
    const [selectedRoomUsers, setSelectedRoomUsers] = useState(null); // Room sélectionnée pour les utilisateurs
    const [searchTerm, setSearchTerm] = useState(""); // Terme de recherche
    const [isFirstVisit, setIsFirstVisit] = useState(true);
    const [role, setRole] = useState(localStorage.getItem("role"));
    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");
    const currentUserId = localStorage.getItem("idUser");
    const navigate = useNavigate();

    useEffect(() => {
        // Vérifier si l'utilisateur est connecté
        if (!token) {
            navigate("/"); // Redirection vers la page de connexion
        }
    }, [navigate, token]);




    const handleOpen = (room) => {
        console.log("Room data on open:", room);
        setSelectedRoom(room);
        setEditableRoom({...room});
        setOpen(!open);
    };




    // Récupérer toutes les Rooms
    const fetchRooms = useCallback(async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/rooms/user-rooms/${currentUserId}`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            setRooms(response.data);
            fetchRoomImages(response.data); // Appeler avec les données récupérées
        } catch (error) {
            console.error("Erreur lors de la récupération des Rooms :", error);
        }
    }, [currentUserId, token]);

    useEffect(() => {
        fetchRooms();
    }, [fetchRooms]);
    const removeUserFromRoom = async (userId) => {
        try {
            await axios.delete(`http://localhost:8080/api/rooms/${selectedRoomUsers}/users/${userId}`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            Swal.fire("Succès", "Utilisateur retiré avec succès.", "success");
            fetchRoomUsers(selectedRoomUsers); // Rafraîchir la liste des utilisateurs
        } catch (error) {
            console.error("Erreur lors de la suppression de l'utilisateur :", error);
            Swal.fire("Erreur", "Impossible de retirer l'utilisateur.", "error");
        }
    };

    const fetchRoomUsers = async (roomId) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/rooms/${roomId}/users`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            setUserList(response.data); // Met à jour la liste des utilisateurs
            setOpenUserModal(true); // Affiche uniquement le modal
        } catch (error) {
            console.error("Erreur lors de la récupération des utilisateurs :", error);
            Swal.fire("Erreur", "Impossible de récupérer les utilisateurs.", "error");
        }
    };


    const joinRoom = async (roomId) => {
        try {
            console.log("Current User ID:", currentUserId);

            await axios.post(
                "http://localhost:8080/api/rooms/join-room",
                { userId: currentUserId, roomId: roomId },
                { headers: { Authorization: `Bearer ${token}` } }
            );

            // Affiche une alerte SweetAlert2
            Swal.fire({
                icon: "success",
                title: "Succès",
                text: "Vous avez rejoint la room avec succès !",
                timer: 2000,
                showConfirmButton: false,
            });

            fetchRooms(); // Rafraîchir la liste des rooms
        } catch (error) {
            console.error("Erreur lors de la tentative de rejoindre la room :", error);

            // Affiche une alerte d'erreur SweetAlert2
            Swal.fire({
                icon: "error",
                title: "Erreur",
                text: "Impossible de rejoindre la room. Veuillez réessayer.",
            });
        }
    };

    const leaveRoom = async (roomId) => {
        try {
            await axios.delete(`http://localhost:8080/api/rooms/${roomId}/leave/${currentUserId}`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            Swal.fire("Succès", "Vous avez quitté la room avec succès.", "success");
            setCurrentRoom(null); // Réinitialiser la Room active
            fetchRooms(); // Rafraîchir la liste des rooms
        } catch (error) {
            console.error("Erreur lors de la tentative de quitter la room :", error);
            Swal.fire("Erreur", "Impossible de quitter la room.", "error");
        }
    };


    // Récupérer les images des rooms
    const fetchRoomImages = async (rooms) => {
        const urls = {};
        for (const room of rooms) {
            if (room.profile) {
                try {
                    const response = await axios.get(`http://localhost:8080/api/${room.profile}`, {
                        headers: {Authorization: `Bearer ${token}`},
                        responseType: "blob",
                    });
                    urls[room.roomId] = URL.createObjectURL(response.data); // Générer l'URL temporaire
                } catch (error) {
                    console.error("Erreur de chargement de l'image :", error);
                    urls[room.roomId] = "default-profile.jpg"; // URL par défaut en cas d'erreur
                }
            } else {
                urls[room.roomId] = "default-profile.jpg"; // URL par défaut si aucune image n'est disponible
            }
        }
        setImageUrls(urls);
    };


    // Récupérer les messages d'une Room
    const fetchMessages = useCallback(
        (roomId) => {
            axios
                .get(`http://localhost:8080/api/rooms/${roomId}`, {
                    headers: {Authorization: `Bearer ${token}`},
                })
                .then((response) => {
                    if (response.data && Array.isArray(response.data)) {
                        setMessages(response.data);
                    }
                })
                .catch((error) =>
                    console.error("Erreur lors de la récupération des messages :", error)
                );
        },
        [token] // Ajoutez uniquement les dépendances nécessaires
    );

    // Récupérer les détails de la room
    const fetchRoomDetails = (roomId) => {
        axios
            .get(`http://localhost:8080/api/rooms/details/${roomId}`, {
                headers: {Authorization: `Bearer ${token}`},
            })
            .then((response) => {
                setRoomDetails(response.data);
                setShowModal(true);
            })
            .catch((error) => console.error("Erreur lors de la récupération des détails de la salle :", error));
    };

    const handleRoomChange = (room) => {
        if (!room || currentRoom?.roomId === room.roomId) return;
        setCurrentRoom(room);
        setMessages([]);
        setIsFirstVisit(false); // Réinitialiser pour ne plus afficher le message d'accueil

        if (room.isMember) {
            fetchMessages(room.roomId); // Charger les messages si l'utilisateur est membre
        }
    };

    // Connexion WebSocket
    const connectWebSocket = useCallback(() => {
        const socket = new SockJS("http://localhost:8080/chat-websocket");
        stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                stompClient.subscribe("/topic/messages", (message) => {
                    const newMessage = JSON.parse(message.body);
                    setMessages((prevMessages) => [...prevMessages, newMessage]);
                });
            },
        });
        stompClient.activate();
    }, []);

    const sendMessage = async () => {
        if (!messageContent.trim() || !currentRoom) return;

        const message = {
            contenu: messageContent,
            userId: currentUserId,
            roomId: currentRoom.roomId,
            username: username,
        };

        try {
            await axios.post("http://localhost:8080/api/messages/send-message", message, {
                headers: {Authorization: `Bearer ${token}`},
            });
            setMessageContent("");
        } catch (error) {
            console.error("Erreur lors de l'envoi du message :", error);
        }
    };

    useEffect(() => {
        fetchRooms();
        connectWebSocket();

        return () => {
            if (stompClient) stompClient.deactivate();
        };
    }, [fetchRooms, connectWebSocket]);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const today = new Date();
        const yesterday = new Date();
        yesterday.setDate(yesterday.getDate() - 1);

        if (date.toDateString() === today.toDateString()) return "Aujourd'hui";
        if (date.toDateString() === yesterday.toDateString()) return "Hier";
        return date.toLocaleDateString("fr-FR", {day: "numeric", month: "long", year: "numeric"});
    };

    const handleAddRoom = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append("titre", newRoom.titre);
        formData.append("description", newRoom.description);
        if (newRoom.profile) {
            formData.append("profile", newRoom.profile);
        }

        try {
            await axios.post("http://localhost:8080/api/rooms/add-room", formData, {
                headers: {Authorization: `Bearer ${token}`},
            });

            Swal.fire("Succès", "La room a été ajoutée avec succès !", "success");
            setOpenAddModal(false);
            fetchRooms();
        } catch (error) {
            console.error("Erreur lors de l'ajout de la room :", error);
            Swal.fire("Erreur", "Impossible d'ajouter la room.", "error");
        }
    };
    const handleUpdateRoom = async () => {
        const formData = new FormData();
        formData.append("titre", editableRoom?.titre || "");
        formData.append("description", editableRoom?.description || "");
        if (editableRoom?.newProfile) {
            formData.append("profile", editableRoom.newProfile);
        }
        try {
            const response = await axios.put(
                `http://localhost:8080/api/rooms/update-room/${editableRoom.roomId}`,
                formData,
                {
                    headers: {Authorization: `Bearer ${token}`, "Content-Type": "multipart/form-data"},
                }
            );

            Swal.fire("Succès", "La room a été mise à jour avec succès !", "success");
            setIsEditing(false);
            setOpen(false);

            // Rafraîchir la liste des rooms
            fetchRooms();

            // Mettre à jour `currentRoom` si c'est la room actuellement active
            if (currentRoom?.roomId === editableRoom.roomId) {
                // Mettre à jour la liste des rooms pour inclure la modification
                setRooms((prevRooms) =>
                    prevRooms.map((room) =>
                        room.roomId === editableRoom.roomId
                            ? {...room, titre: editableRoom.titre, description: editableRoom.description}
                            : room
                    )
                );

                if (currentRoom?.roomId === editableRoom.roomId) {
                    setCurrentRoom((prevRoom) => ({
                        ...prevRoom,
                        titre: editableRoom.titre,
                        description: editableRoom.description,
                    }));
                }

            }
        } catch (error) {
            console.error("Erreur lors de la mise à jour de la room :", error);
            Swal.fire("Erreur", "Impossible de mettre à jour la room.", "error");
        }
    };


    // Grouper les messages par date
    const groupMessagesByDate = () => {
        const groupedMessages = {};
        messages.forEach((message) => {
            const dateKey = new Date(message.dateenvoi).toDateString();
            if (!groupedMessages[dateKey]) groupedMessages[dateKey] = [];
            groupedMessages[dateKey].push(message);
        });
        return groupedMessages;
    };
    const groupedMessages = groupMessagesByDate();
    const filteredRooms = rooms.filter((room) => {
        const lowerSearchTerm = searchTerm.toLowerCase();
        return (
            room.titre.toLowerCase().includes(lowerSearchTerm) || // Filtrer par titre
            (room.code && room.code.toLowerCase().includes(lowerSearchTerm)) // Filtrer par code
        );
    });
    const deleteRoom = async (roomId) => {
        try {
            const confirmation = await Swal.fire({
                title: "Êtes-vous sûr?",
                text: "Cette action est irréversible!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#d33",
                cancelButtonColor: "#3085d6",
                confirmButtonText: "Oui, supprimer",
                cancelButtonText: "Annuler",
            });

            if (confirmation.isConfirmed) {
                await axios.delete(`http://localhost:8080/api/rooms/delete-room/${roomId}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                Swal.fire("Supprimé!", "La room a été supprimée.", "success");
                fetchRooms(); // Rafraîchir la liste des rooms après suppression
            }
        } catch (error) {
            console.error("Erreur lors de la suppression de la room :", error);
            Swal.fire("Erreur", "Impossible de supprimer la room.", "error");
        }
    };

    const handleLogout = () => {
        Swal.fire({
            title: "Êtes-vous sûr ?",
            text: "Vous serez déconnecté de votre compte.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Oui, déconnecter",
            cancelButtonText: "Annuler",
        }).then((result) => {
            if (result.isConfirmed) {
                // Suppression des données locales
                localStorage.removeItem("token");
                localStorage.removeItem("idUser");
                localStorage.removeItem("role");

                // Redirection à la page de login
                navigate("/");
            }
        });
    };


    return (
        <>
            {open && (
                <div className="modal-overlay">
                    <div className="modal-container">
                        {/* En-tête */}
                        <div className="modal-header">
                            {selectedRoom?.titre || "Détails de la Room"}
                        </div>

                        {/* Corps */}
                        <div className="modal-body">
                            {selectedRoom ? (
                                <div className="modal-info">
                                    <div className="image-container">
                                        <img
                                            src={
                                                editableRoom?.newProfile
                                                    ? URL.createObjectURL(editableRoom.newProfile) // Prévisualisation d'un nouveau fichier
                                                    : imageUrls[selectedRoom?.roomId] || "default-profile.jpg" // URL actuelle ou par défaut
                                            }
                                            alt="Room Profile"
                                            className="image-preview"
                                        />

                                    </div>
                                    {isEditing ? (
                                        <>
                                            <label>Image :</label>
                                            <input
                                                type="file"
                                                onChange={(e) => {
                                                    const file = e.target.files[0];
                                                    setEditableRoom({...editableRoom, newProfile: file});
                                                }}
                                                className="input-field mb-2"
                                            />
                                            <label>Titre :</label>
                                            <input
                                                type="text"
                                                value={editableRoom?.titre || ""}
                                                onChange={(e) =>
                                                    setEditableRoom({...editableRoom, titre: e.target.value})
                                                }
                                                className="input-field mb-2"
                                            />

                                            <label>Description :</label>
                                            <textarea
                                                value={editableRoom?.description || ""}
                                                onChange={(e) =>
                                                    setEditableRoom({...editableRoom, description: e.target.value})
                                                }
                                                className="input-field mb-2"
                                            />

                                            <label>Code :</label>
                                            <input
                                                type="text"
                                                value={editableRoom?.code || ""}
                                                disabled
                                                className="input-field bg-gray-200"
                                            />
                                        </>
                                    ) : (
                                        <>
                                            <h3>{selectedRoom?.titre}</h3>
                                            <p>
                                                <strong>Description :</strong>{" "}
                                                {selectedRoom?.description || "Aucune description disponible."}
                                            </p>
                                            <p>
                                                <strong>Code :</strong> {selectedRoom?.code}
                                            </p>
                                        </>
                                    )}
                                </div>
                            ) : (
                                <p>Aucune information disponible.</p>
                            )}
                        </div>
                        {/* Pied */}
                        <div className="modal-footer">
                            {/* Bouton Modifier visible uniquement pour l'administrateur et si l'édition est inactive */}
                            {!isEditing ? (
                                role === "admin" && (
                                    <button className="btn btn-save" onClick={() => setIsEditing(true)}>
                                        <i className="fas fa-edit"></i> Modifier
                                    </button>
                                )
                            ) : (
                                <>
                                    {/* Bouton Enregistrer visible uniquement en mode édition */}
                                    <button className="btn btn-save" onClick={handleUpdateRoom}>
                                        <i className="fas fa-save"></i> Enregistrer
                                    </button>
                                    {/* Bouton Annuler visible uniquement en mode édition */}
                                    <button className="btn btn-cancel" onClick={() => setIsEditing(false)}>
                                        <i className="fas fa-times"></i> Annuler
                                    </button>
                                </>
                            )}

                            {/* Bouton Fermer (visible pour tous les utilisateurs) */}
                            <button className="btn btn-closEEe" onClick={() => setOpen(false)}>
                                <i className="fas fa-times-circle"></i> Fermer
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {openAddModal && (
                <div className="modal-overlay">
                    <div className="modal-container">
                        <h3 className="text-xl font-bold mb-4">Ajouter une nouvelle Room</h3>
                        <form onSubmit={handleAddRoom}>
                            <input
                                type="text"
                                placeholder="Titre"
                                onChange={(e) => setNewRoom({...newRoom, titre: e.target.value})}
                                className="input-field mb-3"
                                required
                            />
                            <textarea
                                placeholder="Description"
                                onChange={(e) => setNewRoom({...newRoom, description: e.target.value})}
                                className="input-field mb-3"
                                required
                            />
                            <input
                                type="file"
                                onChange={(e) => setNewRoom({...newRoom, profile: e.target.files[0]})}
                                className="input-field mb-3"
                            />
                            <div className="modal-footer">
                                <button type="submit" className="btn btn-save">
                                    <i className="fas fa-plus-circle"></i> Ajouter
                                </button>
                                <button onClick={() => setOpenAddModal(false)} className="btn btn-cancel">
                                    <i className="fas fa-times"></i> Annuler
                                </button>

                            </div>
                        </form>
                    </div>
                </div>
            )}
            {openUserModal && (
                <div className="modal-overlay">
                    <div className="modal-container">
                        <h3 className="modal-header">Liste des utilisateurs</h3>
                        <div className="modal-body">
                            {userList.length > 0 ? (
                                <ul className="user-list">
                                    {userList.map((user) => (
                                        <li key={user.userId} className="user-item">
                                            <span>{user.fullname} ({user.username})</span>
                                            {role === "admin" && (
                                                <button
                                                    onClick={() => removeUserFromRoom(user.userId)}
                                                    className="btn btn-danger"
                                                >
                                                    <i className="fas fa-user-minus"></i> Retirer
                                                </button>
                                            )}
                                        </li>
                                    ))}
                                </ul>

                            ) : (
                                <p>Aucun utilisateur dans cette Room.</p>
                            )}
                        </div>
                        <div className="modal-footer">
                            <button onClick={() => setOpenUserModal(false)} className="btn btn-closEEe">
                                <i className="fas fa-times-circle"></i> Fermer
                            </button>
                        </div>
                    </div>
                </div>
            )}
            <div className="room-container">
                {/* Sidebar - Liste des Rooms */}
                <div className="sidebar">
                    <div className="logo-container">
                        <img
                            src="/logo-white.png"
                            alt="Logo"
                            className="sidebar-logo"
                        />
                    </div>
                    {/* Champ de recherche */}
                    <div className="search-container">
                        <div className="search-input-wrapper">
                            <i className="fas fa-search search-icon"></i> {/* Icône FontAwesome */}
                            <input
                                type="text"
                                placeholder="Rechercher par nom ou code..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="search-input"
                            />
                        </div>
                    </div>


                    {/* Liste filtrée des Rooms */}
                    <ul className="room-list">
                        {filteredRooms.map((room) => (
                            <li
                                key={room.roomId}
                                className={`room-item ${currentRoom?.roomId === room.roomId ? "active" : ""}`}
                                onClick={() => room.isMember && handleRoomChange(room)} // Active seulement si membre
                            >
                                {/* Image de profil */}
                                <img
                                    src={imageUrls[room.roomId] || "default-profile.jpg"}
                                    alt="Room"
                                    className="room-profile"
                                />
                                <span>{room.titre}</span>

                                {/* Bouton Rejoindre */}
                                {!room.isMember && (
                                    <button className="join-button" onClick={() => joinRoom(room.roomId)}>
                                        <i className="fas fa-user-plus icon"></i> Rejoindre
                                    </button>
                                )}

                                {/* Bouton Détails */}
                                <button className="details-button" onClick={() => handleOpen(room)}>
                                    <i className="fas fa-info-circle icon"></i> Détails
                                </button>

                                {/* Bouton Utilisateurs */}
                                <button
                                    className="details-button"
                                    onClick={() => fetchRoomUsers(room.roomId)}
                                >
                                    <i className="fas fa-users icon"></i> Utilisateurs
                                </button>

                                {/* Bouton Supprimer */}
                                {role === "admin" && (
                                    <button
                                        className="delete-button"
                                        onClick={(e) => {
                                            e.stopPropagation(); // Empêche la sélection de la room
                                            deleteRoom(room.roomId);
                                        }}
                                    >
                                        <i className="fas fa-trash icon"></i> Supprimer
                                    </button>
                                )}

                            </li>
                        ))}
                    </ul>

                    {role === "admin" && (
                        <div className="sidebar-footer">
                            <button onClick={() => setOpenAddModal(true)} className="btn btn-add">
                                <i className="fas fa-plus icon"></i> Ajouter Room
                            </button>
                        </div>
                    )}


                </div>

                {/* Chat Section */}
                <div className="chat-section">
                    {/* Header de la Room */}
                    {currentRoom && (
                        <div className="chat-header">
                            <img
                                src={imageUrls[currentRoom.roomId] || "default-profile.jpg"}
                                alt="Profile"
                                className="chat-header-img"
                            />
                            <h3>{currentRoom.titre}</h3> {/* Affichage dynamique du titre */}

                            <div className="button-group">
                                <button className="btn-danger" onClick={() => leaveRoom(currentRoom.roomId)}>
                                    <i className="fas fa-sign-out-alt"></i> Quitter la Room
                                </button>
                                <button className="btn btn-danger" onClick={() => handleLogout()}>
                                    <i className="fas fa-sign-out-alt"></i> Déconnexion
                                </button>
                            </div>

                        </div>
                    )}


                    {/* Messages */}
                    <div className="messages-container">
                        {/* Message de bienvenue pour la première visite */}
                        {isFirstVisit ? (
                            <div className="welcome-message">
                                <h2>Bonjour dans ISI Talk</h2>
                                <p>Bienvenue dans votre application de chat.</p>
                            </div>
                        ) : !currentRoom ? (
                            /* Cas où aucune room n'est sélectionnée */
                            <div className="no-room-selected-message">
                                <h3>Aucune room sélectionnée</h3>
                                <p>Veuillez sélectionner une room pour voir les messages.</p>
                            </div>
                        ) : !currentRoom.isMember ? (
                            /* Cas où l'utilisateur n'est pas membre de la room */
                            <div className="non-member-message">
                                <h3>Vous n'êtes pas membre de cette room.</h3>
                                <p>Essayez de rejoindre pour participer à la discussion.</p>
                            </div>
                        ) : messages.length > 0 ? (
                            /* Cas où il y a des messages à afficher */
                            Object.keys(groupedMessages).map((dateKey, index) => (
                                <div key={index}>
                                    {/* Séparateur de date */}
                                    <div className="date-separator">
                                        <span>{formatDate(dateKey)}</span>
                                    </div>

                                    {/* Affichage des messages pour chaque date */}
                                    {groupedMessages[dateKey].map((message, msgIndex) => {
                                        const isMine = message.idUser.toString() === currentUserId;
                                        return (
                                            <div
                                                key={msgIndex}
                                                className={`message ${
                                                    isMine ? "message-outgoing" : "message-incoming"
                                                }`}
                                            >
                                                {/* Afficher le nom de l'utilisateur pour les messages entrants */}
                                                {!isMine && (
                                                    <small className="message-username">{message.username}</small>
                                                )}

                                                <p>{message.contenu}</p>
                                                <small className="message-time">
                                                    {new Date(message.dateenvoi).toLocaleTimeString([], {
                                                        hour: "2-digit",
                                                        minute: "2-digit",
                                                    })}
                                                </small>
                                            </div>
                                        );
                                    })}
                                </div>
                            ))
                        ) : (
                            /* Cas où la room est sélectionnée mais n'a aucun message */
                            <div className="no-messages-message">
                                <h3>Aucun message à afficher</h3>
                                <p>Soyez le premier à envoyer un message dans cette room !</p>
                            </div>
                        )}
                    </div>

                    {/* Input pour envoyer un message */}
                    {currentRoom?.isMember && (
                        <div className="input-section">
                                <textarea
                                    placeholder="Tapez votre message..."
                                    value={messageContent}
                                    onChange={(e) => setMessageContent(e.target.value)}
                                    rows={1}
                                    onKeyDown={(e) => {
                                        if (e.key === "Enter" && !e.shiftKey) {
                                            e.preventDefault();
                                            sendMessage();
                                        }
                                    }}
                                />
                            <button className="send-button" onClick={sendMessage}>
                                <FaPaperPlane/>
                            </button>
                        </div>
                    )}

                </div>
            </div>
        </>
    );
}

export default Room;
