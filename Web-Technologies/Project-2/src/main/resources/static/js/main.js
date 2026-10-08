/**
 * Toggles the navigation menu when the burger menu icon is clicked.
 * Also closes the menu when clicking outside of it.
 */
document.addEventListener('DOMContentLoaded', function() {
    const burgerMenu = document.querySelector('.burger-menu');
    const navMenu = document.querySelector('nav ul');

    if (burgerMenu && navMenu) {
        burgerMenu.addEventListener('click', function() {
            this.classList.toggle('active');
            navMenu.classList.toggle('active');
        });

        // Close menu when clicking outside
        document.addEventListener('click', function(event) {
            if (!event.target.closest('nav') && navMenu.classList.contains('active')) {
                burgerMenu.classList.remove('active');
                navMenu.classList.remove('active');
            }
        });
    }
});

/**
 * Alerts with the class 'alert' will automatically fade out
 * and be removed from the DOM after 6 seconds.
 */
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.remove();
            }, 500);
        }, 6000);
    });
});


/**
 * Forms with the attribute data-confirm will show a confirmation dialog
 * before submission. The value of the attribute is the confirmation message.
 * It is used in the admin client to confirm an advertisement deactivation.
 */
document.addEventListener('DOMContentLoaded', function() {
    const dangerousForms = document.querySelectorAll('form[data-confirm]');
    dangerousForms.forEach(form => {
        form.addEventListener('submit', function(event) {
            const message = this.getAttribute('data-confirm');
            if (!confirm(message)) {
                event.preventDefault();
            }
        });
    });
});