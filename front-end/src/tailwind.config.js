module.exports = {
    content: [
        "./src/**/*.{js,jsx,ts,tsx}", // Inclut tous les fichiers React
    ],
    theme: {
        extend: {},
    },
    plugins: [
        require('@tailwindcss/forms'), // Facultatif pour des styles de formulaire
    ],
};
