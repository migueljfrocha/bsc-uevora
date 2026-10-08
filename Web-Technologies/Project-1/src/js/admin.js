const adminRestaurantLink = "https://magno.di.uevora.pt/tweb/t1/admin/restaurante/list";
const adminClientLink = "https://magno.di.uevora.pt/tweb/t1/admin/cliente/list";
const adminOfferLink = "https://magno.di.uevora.pt/tweb/t1/admin/oferta/list";
const googleMapsBaseUrl = "https://www.google.com/maps?q=";

let xhr = new XMLHttpRequest();
let content = document.getElementById("content");

let restaurantCurrentPage = 0;
let offersCurrentPage = 0;
let clientsCurrentPage = 0;


// -------------------------------------------------- RESTAURANTS --------------------------------------------------


/**
 * Displays the list of restaurants in the admin panel with pagination and filtering options.
 * Sends a POST request to the admin restaurant list endpoint and renders the received data.
 * Handles pagination through "Next" and "Previous" buttons.
 */
function showRestaurantsAdmin() {
  offersCurrentPage = 0; // Reset offers page
  clientsCurrentPage = 0; // Reset clients page

  xhr.open("POST", adminRestaurantLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let data = `page=${restaurantCurrentPage}`;

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const restaurants = JSON.parse(this.responseText);

      let output = restaurantFormInit();

      restaurants.restaurante_set.forEach(function (restaurant) {
        output += renderAdminRestaurantCard(restaurant);
      });

      let isPrevPage = "";
      if (restaurants.page == 0) {
        isPrevPage = "disabled";
      }
      let isNextPage = "";
      if (restaurants.page == restaurants.last_page) {
        isNextPage = "disabled";
      }
      output += "</ul>";
      output += ` <div id="list-menu">
                        <button onclick="restaurantPrevPage()" id="restaurants-prev-page" ${isPrevPage}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="24px"
                            viewBox="0 -960 960 960"
                            width="24px"
                            fill="#000000"
                          >
                            <path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" />
                          </svg>
                        </button>
                        <span id="page-info">
                          Página <strong>${restaurants.page + 1}</strong> de ${restaurants.last_page + 1}
                        </span>
                        <button onclick="restaurantNextPage()" id="restaurants-next-page" ${isNextPage}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="24px"
                            viewBox="0 -960 960 960"
                            width="24px"
                            fill="#000000"
                          >
                            <path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" />
                          </svg>
                        </button>
                      </div>
                    </div>`;
      content.innerHTML = output;
    }
  };
  xhr.send(data);
}


/**
 * Renders an individual restaurant card for the admin panel.
 * @param {Object} restaurant, containing restaurant details.
 * @returns {string} HTML string for the restaurant card.
 */
function renderAdminRestaurantCard(restaurant) {
  const name = restaurant.nome;
  const id = restaurant.restaurante_id;
  const loc = restaurant.localizacao;
  const registerDate = restaurant.data_de_registo;
  const owner = restaurant.proprietario;

  const mapUrl = `${googleMapsBaseUrl}${loc.lat},${loc.long}`;

  return `
          <li class="generic-card">
            <div class="generic-card-header">
              <h3>${name}</h3>
              <span class="generic-id">ID ${id}</span>
            </div>
            <hr />
            <div class="restaurant-admin">
              <p><strong>Proprietário: </strong>${owner}</p>
              <p><strong>Data de registo: </strong>${registerDate}</p>
            </div>
            <div class="restaurant-location">
              <p><strong>Morada:</strong> ${loc.morada}</p>
              <p><strong>Código Postal:</strong> ${loc.cod_postal}</p>
              <p><strong>Latitude:</strong>${loc.lat}</p>
              <p><strong>Longitude:</strong>${loc.long}</p>
            </div>
            <div class="restaurant-maps">
              <a href="${mapUrl}" target="_blank">
                Ver no Google Maps
              </a>
            </div>
          </li>`;
}


/**
 * Resets the restaurant filter and displays the full restaurant list.
 */
function resetRestaurantFilter() {
  let restaurantCurrentPage = 0;
  showRestaurantsAdmin();
}

/**
 * Submits the restaurant filter form and displays the filtered restaurant list.
 * Sends a GET request to the admin restaurant list endpoint and processes the response.
 * Filters restaurants based on the selected criteria (nome or morada).
 * Search is case-insensitive for names and addresses.
 * Renders the filtered list in the admin panel.
 */
function submitRestaurantFilter() {
  xhr.open("GET", adminRestaurantLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let filterType = document.getElementById("filter-type").value;
  let toFilter = document.getElementById("toFilter").value;

  let filteredRestaurants = [];

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const restaurants = JSON.parse(this.responseText);
      let output = restaurantFormInit();

      // Filter the restaurants based on the criteria
      if (filterType === "name") {
        restaurants.restaurante_set.forEach(function (restaurant) {
          if (restaurant.nome.toLowerCase().includes(toFilter.toLowerCase())) {
            filteredRestaurants.push(restaurant);
          }
        });
      } else if (filterType === "address") {
        restaurants.restaurante_set.forEach(function (restaurant) {
          if (
            restaurant.localizacao.morada
              .toLowerCase()
              .includes(toFilter.toLowerCase())
          ) {
            filteredRestaurants.push(restaurant);
          }
        });
      }

      filteredRestaurants.forEach(function (restaurant) {
        output += renderAdminRestaurantCard(restaurant);
      });

      output += "</ul>";
      content.innerHTML = output;
    }
  };
  xhr.send();
}


/**
 * Initializes the restaurant list form with filtering options.
 * @returns {string} HTML string for the restaurant list form.
 */
function restaurantFormInit() {
  return (output = `
            <div id="list-container">
              <h1>Restaurantes</h1>    
              <div id="list-filters">
              <form id="filter-form" onsubmit="return false;">
                <div class="form-group">
                  <input type="text" id="toFilter" name="toFilter" required>
                </div>
                <div class="form-group">
                  <select id="filter-type" name="filter_type" required>
                    <option value="name">Nome</option>
                    <option value="address">Morada</option>
                  </select>
                </div>
                  <button type="button" id="submit-filter-button" onclick="submitRestaurantFilter()">Filtrar</button>
                  <button type="button" id="reset-filter-button" onclick="resetRestaurantFilter()">Reset</button>
              </form>
            </div>
            <ul id="generic-card-list">`);
}


/**
 * Handles pagination for restaurant list.
 * Increases the current page and refreshes the restaurant list display.
 * Scrolls to the top of the content area for better user experience.
 */
function restaurantNextPage() {
  restaurantCurrentPage += 1;
  showRestaurantsAdmin();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for restaurant list.
 * Decreases the current page and refreshes the restaurant list display.
 * Scrolls to the top of the content area for better user experience.
 */
function restaurantPrevPage() {
  restaurantCurrentPage -= 1;
  showRestaurantsAdmin();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


// -------------------------------------------------- OFFERS --------------------------------------------------


/**
 * Displays the list of offers in the admin panel with pagination and filtering options.
 * Sends a POST request to the admin offer list endpoint and renders the received data.
 * Handles pagination through "Next" and "Previous" buttons.
 * Resets restaurant and client pages to the first page when called.
 */
function showOffersAdmin() {
  restaurantCurrentPage = 0; // Reset restaurant page
  clientsCurrentPage = 0; // Reset clients page

  xhr.open("POST", adminOfferLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let data = `page=${offersCurrentPage}`;

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const offers = JSON.parse(this.responseText);

      let output = offerFormInit();

      offers.oferta_set.forEach(function (offer) {
        output += renderAdminOfferCard(offer);
      });

      let isPrevPage = "";
      if (offers.page == 0) {
        isPrevPage = "disabled";
      }
      let isNextPage = "";
      if (offers.page == offers.last_page) {
        isNextPage = "disabled";
      }

      output += "</ul>";
      output += ` <div id="list-menu">
                        <button onclick="offersPrevPage()" id="offers-prev-page" ${isPrevPage}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="24px"
                            viewBox="0 -960 960 960"
                            width="24px"
                            fill="#000000"
                          >
                            <path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" />
                          </svg>
                        </button>
                        <span id="page-info">Página <strong>${
                          offers.page + 1
                        }</strong> de ${offers.last_page + 1}</span>
                        <button onclick="offersNextPage()" id="offers-next-page" ${isNextPage}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="24px"
                            viewBox="0 -960 960 960"
                            width="24px"
                            fill="#000000"
                          >
                            <path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" />
                          </svg>
                        </button>
                      </div>
                    </div>`;
      content.innerHTML = output;
    }
  };
  xhr.send(data);
}


/**
 * Renders an offer card for the admin panel.
 * @param {Object} offer, that contains offer details.
 * @returns {string} HTML string for the offer card.
 */
function renderAdminOfferCard(offer) {
  const name = offer.nome;
  const offer_id = offer.oferta_id;
  const offer_image = offer.foto;
  const offer_description = offer.descricao;
  const restaurant_id = offer.restaurante_id;
  const units = offer.unidades;

  return `
        <li class="offer-card">
          <img 
            src="${offer_image}" 
            alt="${name}" 
            class="offer-image" 
            onerror="errorLoadingImage(this);"
          />
          <div class="offer-header">
            <div class="offer-title">
              <h3>${name}</h3>
              <p class="offer-restaurant-name">Restaurante #${restaurant_id}</p>
            </div>
            <span class="offer-id">ID ${offer_id}</span>
          </div>
          <hr />
          <div class="offer-info">
            <p class="offer-description">
              ${offer_description}
            </p>
            <span class="offer-units">${units} Unidades</span>
          </div>
        </li>`;
}


/**
 * Resets the offer filter and displays the full offer list.
 */
function resetOfferFilter() {
  let offersCurrentPage = 0;
  showOffersAdmin();
}


/**
 * Submits the offer filter form and displays the filtered offer list.
 * Sends a GET request to the admin offer list endpoint and processes the response.
 * Filters offers based on the selected criteria (nome or restaurante_id).
 * Renders the filtered list in the admin panel.
 */
function submitOfferFilter() {
  xhr.open("GET", adminOfferLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let filterType = document.getElementById("filter-type").value;
  let toFilter = document.getElementById("toFilter").value;

  let filteredOffers = [];

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const offers = JSON.parse(this.responseText);
      let output = offerFormInit();

      // Filter the offers based on the criteria
      if (filterType === "name") {
        offers.oferta_set.forEach(function (offer) {
          if (offer.nome.toLowerCase().includes(toFilter.toLowerCase())) {
            filteredOffers.push(offer);
          }
        });
      } else if (filterType === "generic-id") {
        offers.oferta_set.forEach(function (offer) {
          if (offer.restaurante_id == toFilter.toLowerCase()) {
            filteredOffers.push(offer);
          }
        });
      }

      filteredOffers.forEach(function (restaurant) {
        output += renderAdminOfferCard(restaurant);
      });

      output += "</ul>";
      content.innerHTML = output;
    }
  };
  xhr.send();
}


/**
 * Initializes the offer list form with filtering options.
 * @returns {string} HTML string for the offer list form.
 */
function offerFormInit() {
  let output = `
          <div id="list-container">
            <h1>Ofertas</h1>    
            <div id="offer-list-filters">
            <form id="filter-form" onsubmit="return false;">
              <div class="form-group">
                <input type="text" id="toFilter" name="toFilter" required>
              </div>
              <div class="form-group">
                <select id="filter-type" name="filter_type" required>
                  <option value="name">Nome</option>
                  <option value="generic-id">Restaurante ID</option>
                </select>
              </div>
                <button type="button" id="submit-filter-button" onclick="submitOfferFilter()">Filtrar</button>
                <button type="button" id="reset-filter-button" onclick="resetOfferFilter()">Reset</button>
            </form>
          </div>
          <ul id="offer-list">`;
  return output;
}


/**
 * Handles pagination for offer list.
 * Increases the current page and refreshes the offer list display.
 * Scrolls to the top of the content area for better user experience.
 */
function offersNextPage() {
  offersCurrentPage += 1;
  showOffersAdmin();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for offer list.
 * Decreases the current page and refreshes the offer list display.
 * Scrolls to the top of the content area for better user experience.
 */
function offersPrevPage() {
  offersCurrentPage -= 1;
  showOffersAdmin();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


// -------------------------------------------------- CLIENTS --------------------------------------------------


/**
 * Displays the list of clients in the admin panel with pagination and filtering options.
 * Sends a POST request to the admin client list endpoint and renders the received data.
 * Handles pagination through "Next" and "Previous" buttons.
 * Resets restaurant and offer pages to the first page when called.
 */
function showClientsAdmin() {
  restaurantCurrentPage = 0; // Reset restaurant page
  offersCurrentPage = 0; // Reset offers page

  xhr.open("POST", adminClientLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  let data = `page=${clientsCurrentPage}`;
  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const clients = JSON.parse(this.responseText);

      let output = clientFormInit();

      clients.cliente_set.forEach(function (client) {
        output += renderAdminClientCard(client);
      });

      let isPrevPage = "";
      if (clients.page == 0) {
        isPrevPage = "disabled";
      }

      let isNextPage = "";
      if (clients.page == clients.last_page) {
        isNextPage = "disabled";
      }
      output += "</ul>";
      output += ` <div id="list-menu">
                        <button onclick="clientsPrevPage()" id="clients-prev-page" ${isPrevPage}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="24px"
                            viewBox="0 -960 960 960"
                            width="24px"
                            fill="#000000"
                          >
                            <path d="M560-240 320-480l240-240 56 56-184 184 184 184-56 56Z" />
                          </svg>
                        </button>
                        <span id="page-info">Página <strong>${clients.page + 1}</strong> de ${clients.last_page + 1}</span>
                        <button onclick="clientsNextPage()" id="clients-next-page" ${isNextPage}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            height="24px"
                            viewBox="0 -960 960 960"
                            width="24px"
                            fill="#000000"
                          >
                            <path d="M504-480 320-664l56-56 240 240-240 240-56-56 184-184Z" />
                          </svg>
                        </button>
                      </div>
                    </div>`;
      content.innerHTML = output;
    }
  };
  xhr.send(data);
}


/**
 * Renders an individual client card for the admin panel.
 * @param {Object} client, containing client details.
 * @returns {string} HTML string for the client card.
 */
function renderAdminClientCard(client) {
  const name = client.nome;
  const id = client.cliente_id;
  const username = client.username;
  const registerDate = client.data_de_registo;

  return `
          <li class="generic-card">
            <div class="generic-card-header">
              <h3>${name}</h3>
              <span class="generic-id">ID ${id}</span>
            </div>
            <hr />
            <div class="restaurant-admin">
              <p><strong>Username: </strong>${username}</p>
              <p><strong>Data de registo: </strong>${registerDate}</p>
            </div>
          </li>`;
}


/**
 * Resets the client filter and displays the full client list.
 */
function resetClientFilter() {
  let clientsCurrentPage = 0;
  showClientsAdmin();
}


/**
 * Submits the client filter form and displays the filtered client list.
 * Sends a GET request to the admin client list endpoint and processes the response.
 * Filters clients based on the selected criteria (nome or cliente_id).
 */
function submitClientFilter() {
  xhr.open("GET", adminClientLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let filterType = document.getElementById("filter-type").value;
  let toFilter = document.getElementById("toFilter").value;

  let filteredClients = [];

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const clients = JSON.parse(this.responseText);
      let output = clientFormInit();

      // Filter the clients based on the criteria
      if (filterType === "name") {
        clients.cliente_set.forEach(function (client) {
          if (client.nome.toLowerCase().includes(toFilter.toLowerCase())) {
            filteredClients.push(client);
          }
        });
      } else if (filterType === "client-id") {
        clients.cliente_set.forEach(function (client) {
          if (client.cliente_id == toFilter.toLowerCase()) {
            filteredClients.push(client);
          }
        });
      }

      filteredClients.forEach(function (client) {
        output += renderAdminClientCard(client);
      });

      output += "</ul>";
      content.innerHTML = output;
    }
  };
  xhr.send();
}


/**
 * Initializes the client list form with filtering options.
 * @returns {string} HTML string for the client list form.
 */
function clientFormInit() {
  let output = `
          <div id="list-container">
            <h1>Clientes</h1>    
            <div id="list-filters">
            <form id="filter-form" onsubmit="return false;">
              <div class="form-group">
                <input type="text" id="toFilter" name="toFilter" required>
              </div>
              <div class="form-group">
                <select id="filter-type" name="filter_type" required>
                  <option value="name">Nome</option>
                  <option value="client-id">Cliente ID</option>
                </select>
              </div>
                <button type="button" id="submit-filter-button" onclick="submitClientFilter()">Filtrar</button>
                <button type="button" id="reset-filter-button" onclick="resetClientFilter()">Reset</button>
            </form>
          </div>
          <ul id="generic-card-list">`;
  return output;
}


/**
 * Handles pagination for client list.
 * Increases the current page and refreshes the client list display.
 * Scrolls to the top of the content area for better user experience.
 */
function clientsNextPage() {
  clientsCurrentPage += 1;
  showClientsAdmin();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for client list.
 * Decreases the current page and refreshes the client list display.
 * Scrolls to the top of the content area for better user experience.
 */
function clientsPrevPage() {
  clientsCurrentPage -= 1;
  showClientsAdmin();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


// -------------------------------------------------- UTILITIES --------------------------------------------------


/**
 * Handles image loading errors by replacing the failed image with a default placeholder.
 * @param {HTMLImageElement} image - The image element that failed to load.
 */
function errorLoadingImage(image) {
  image.onerror = null; // Avoiding an infinite loop if the placeholder also fails
  image.src = "../assets/img/default-offer-image-placeholder.png";
}
