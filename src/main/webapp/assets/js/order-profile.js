
function toggleOrdersForm() {
    const ordersForm = document.getElementById("ordersForm");
    const profileForm = document.getElementById("profileForm");
    const passwordForm = document.getElementById("passwordForm");
    const details = document.getElementById("profileDetails");

    // Toggle visibility of forms
    ordersForm.style.display = ordersForm.style.display === "none" ? "block" : "none";
    profileForm.style.display = "none";
    passwordForm.style.display = "none";
    details.style.display = "none";

    // Fetch orders if the form is being shown
    if (ordersForm.style.display === "block") {
        $.ajax({
            url: "/api/orders/customer",
            method: "GET",
            success: function (data) {
                populateOrdersTable(data);
            },
            error: function () {
                alert("Failed to load orders. Please try again.");
            }
        });
    }
}


// Format the date to a more readable format
function formatOrderDate(dateStr) {
    const date = new Date(dateStr);
    const options = {
        year: 'numeric',
        month: 'short',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    };
    return new Intl.DateTimeFormat('en-US', options).format(date);
}

// Populate the table with order data
function populateOrdersTable(orders) {
    const ordersTableBody = document.getElementById("ordersTableBody");
    ordersTableBody.innerHTML = ""; // Clear existing rows
    console.log("Orders:", orders);

    orders.forEach(order => {
        const formattedDate = formatOrderDate(order.date);
        const formattedPrice = (order.totalPrice / 100).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        const row = `<tr id="order-row-${order.id}">
            <td>${order.id}</td>
            <td>${formattedPrice}</td>
            <td>${formattedDate}</td>
            <td>${order.state}</td>
            <td>
                <button class="btn btn-success" onclick="viewOrderDetails(${order.id}, event)">View</button>
            </td>
        </tr>`;
        ordersTableBody.insertAdjacentHTML("beforeend", row);
    });
}


function closeOrderModal() {
    const modal = document.getElementById("orderDetailsModal");
    modal.style.display = "none";
}


function viewOrderDetails(orderId, event) {
    event.preventDefault();
    console.log("viewOrderDetails called with orderId:", orderId);
    const orderDetailsModal = document.getElementById("orderDetailsModal");
    const ordersForm = document.getElementById("ordersForm");
    const profileForm = document.getElementById("profileForm");
    const passwordForm = document.getElementById("passwordForm");
    const details = document.getElementById("profileDetails");

    orderDetailsModal.style.display = orderDetailsModal.style.display === "none" ? "block" : "none";
    ordersForm.style.display = ordersForm.style.display = "block";
    profileForm.style.display = "none";
    passwordForm.style.display = "none";
    details.style.display = "none";

    // Fetch specific order details
    if (orderDetailsModal.style.display === "block"){
        $.ajax({
            url: `/api/orders/${orderId}`,  // Adjust the endpoint if necessary
            method: "GET",
            success: function (data) {
                console.log("Order details received:", data);
                populateOrderDetails(data);
            },
            error: function (error) {
                console.error("Error fetching order details:", error);
                alert("Failed to load order details. Please try again.");
            }
        });
    }
}

function populateOrderDetails(order) {
    const orderDetailsBody = document.getElementById("orderDetailsBody");

    const items = Array.isArray(order.orderItems) ? order.orderItems : [];

    // Format and display the order details
    const detailsHTML = `
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Total Price</th>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${order.id}</td>
                    <td>${(order.totalPrice / 100).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</td>
                    <td>${new Date(order.date).toLocaleDateString()}</td>
                    <td>${order.state}</td>
                </tr>
            </tbody>
        </table>
        <h5>Items:</h5>
                <ul>
                    ${items.map(item => `
                        <li>
                                <table class="table table-striped">
                    <tbody>
                <tr>
                    <td><img src="${item.product.image}" alt="product img" width="100px"></td>
                    <td style="text-align: start">                            
                        <strong>Product:</strong> ${item.product.name}
                        <br> 
                        <strong>Quantity:</strong> ${item.quantity}
                        <br> 
                        <strong>Price:</strong> ${(item.currentPrice / 100).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </td>
                </tr>
            </tbody>
        </table>
                            <hr>
                        </li>
                    `).join('')}
                </ul>
    `;
    orderDetailsBody.innerHTML = detailsHTML;
}


