
function showNotification(message, type = 'success', duration = 3000) {
    const container = document.getElementById('notification-container');

    // Create notification element
    const notification = document.createElement('div');
    notification.classList.add('notification', type);

    // Add message and dismiss button
    notification.innerHTML = `
        <span>${message}</span>
        <span class="dismiss">&times;</span>
    `;

    // Append notification to container
    container.appendChild(notification);

    // Dismiss notification on click
    notification.querySelector('.dismiss').onclick = () => {
        notification.style.opacity = '0';
        notification.addEventListener('transitionend', () => notification.remove());
    };

    // Auto-remove notification after the specified duration
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.addEventListener('transitionend', () => notification.remove());
    }, duration);
}