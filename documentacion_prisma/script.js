document.addEventListener('DOMContentLoaded', () => {
    const secciones = document.querySelectorAll('section');
    
    const opciones = {
        threshold: 0.15
    };

    const observador = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, opciones);

    secciones.forEach(seccion => {
        observador.observe(seccion);
    });

    // Efecto de scroll suave para enlaces (si se añaden en el futuro)
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });
});
