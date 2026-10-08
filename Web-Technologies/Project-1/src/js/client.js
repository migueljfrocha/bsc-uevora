const clientRestaurantListLink =
  "https://magno.di.uevora.pt/tweb/t1/restaurante/list";
const clientRestaurantSearchLink =
  "https://magno.di.uevora.pt/tweb/t1/restaurante/search";

const clientClientLink = "https://magno.di.uevora.pt/tweb/t1/cliente/list";

const clientOfferLink = "https://magno.di.uevora.pt/tweb/t1/oferta/list";
const clientOfferSearchLink =
  "https://magno.di.uevora.pt/tweb/t1/oferta/search";

const clientReserveOffer = "https://magno.di.uevora.pt/tweb/t1/oferta/reserve";

const googleMapsBaseUrl = "https://www.google.com/maps?q=";

let xhr = new XMLHttpRequest();
let content = document.getElementById("content");

let restaurantCurrentPage = 0;
let offersCurrentPage = 0;
let restaurantSearchPage = 0;
let offersSearchPage = 0;

let prevFilter = {
  toFilter: "",
  filterType: "",
};

// -------------------------------------------------- RESTAURANTS --------------------------------------------------


/**
 * Fetch and display restaurant list for the client.
 * Sends a POST request to the client restaurant list endpoint and renders the received data.
 * Updates the content div with the list of restaurants and pagination controls.
 */
function showRestaurantsClient() {
  offersCurrentPage = 0; // Reset offers page

  xhr.open("POST", clientRestaurantListLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let data = `page=${restaurantCurrentPage}`;

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const restaurants = JSON.parse(this.responseText);

      let output = restaurantFormInit();

      restaurants.restaurante_set.forEach(function (restaurant) {
        output += renderRestaurantCard(restaurant);
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
                    <button onclick="restaurantClientPrevPage()" id="restaurants-prev-page" ${isPrevPage}>
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
                    <button onclick="restaurantClientNextPage()" id="restaurants-next-page" ${isNextPage}>
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
 * Renders a restaurant card.
 * @param {Object} restaurant, that contains restaurant details.
 * @returns {string} HTML string for the restaurant card.
 */
function renderRestaurantCard(restaurant) {
  const name = restaurant.nome;
  const id = restaurant.restaurante_id;
  const loc = restaurant.localizacao;
  const mapUrl = `${googleMapsBaseUrl}${loc.lat},${loc.long}`;

  return `
    <li class="generic-card">
      <div class="generic-card-header">
        <h3>${name}</h3>
        <span class="generic-id">ID ${id}</span>
      </div>
      <hr />
      <div class="restaurant-location">
        <p><strong>Morada:</strong> ${loc.morada}</p>
        <p><strong>Código Postal:</strong> ${loc.cod_postal}</p>
      </div>
      <div class="restaurant-maps">
        <a href="${mapUrl}" target="_blank">
          Ver no Google Maps
        </a>
      </div>
    </li>`;
}


/**
 * Resets the current page and search page to 0 and displays the full restaurant list.
 */
function resetRestaurantSearch() {
  restaurantCurrentPage = 0;
  restaurantSearchPage = 0;
  showRestaurantsClient();
}


/**
 * Submits the restaurant search form.
 * Sends a POST request to the client restaurant search endpoint with the current filter.
 * Updates the content div with the search results and pagination controls.
 * @param {boolean} fromPagination - Indicates if the submission is from pagination controls.
 */
function submitRestaurantSearch(fromPagination = false) {
  xhr.open("POST", clientRestaurantSearchLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  // If not from pagination (next or previous page), update prevFilter and reset page
  if (!fromPagination) {
    let currentType = document.getElementById("filter-type").value;
    let currentVal = document.getElementById("toFilter").value;

    updatePrevFilter(currentType, currentVal);
    restaurantSearchPage = 0;
  }

  // Encode toFilter to handle special characters
  let encodedToFilter = encodeURIComponent(prevFilter.toFilter);
  let data = `${prevFilter.filterType}=${encodedToFilter}&page=${restaurantSearchPage}`;

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const restaurants = JSON.parse(this.responseText);
      let output = restaurantFormInit();

      restaurants.restaurante_set.forEach(function (restaurant) {
        output += renderRestaurantCard(restaurant);
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
                    <button onclick="restaurantSearchPrevPage()" id="restaurants-prev-page" ${isPrevPage}>
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
                    <span id="page-info">Página <strong>${restaurants.page + 1}</strong> de ${restaurants.last_page + 1}</span>
                    <button onclick="restaurantSearchNextPage()" id="restaurants-next-page" ${isNextPage}>
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

      document.getElementById("toFilter").value = prevFilter.toFilter;
      document.getElementById("filter-type").value = prevFilter.filterType;
    }
  };
  xhr.send(data);
}


/**
 * Handles pagination for restaurant search results.
 * Decrements the search page and submits the search form.
 * Scrolls to the top of the content area for better user experience.
 */
function restaurantSearchPrevPage() {
  restaurantSearchPage--;
  submitRestaurantSearch(true);
  window.scrollTo({ top: 50, behavior: "smooth" });
}

/**
 * Handles pagination for restaurant search results.
 * Increments the search page and submits the search form.
 * Scrolls to the top of the content area for better user experience.
 */
function restaurantSearchNextPage() {
  restaurantSearchPage++;
  submitRestaurantSearch(true);
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for the full restaurant list.
 * Decrements the current page and displays the restaurant list.
 * Scrolls to the top of the content area for better user experience.
 */
function restaurantClientPrevPage() {
  restaurantCurrentPage--;
  showRestaurantsClient();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for the full restaurant list.
 * Increments the current page and displays the restaurant list.
 * Scrolls to the top of the content area for better user experience.
 */
function restaurantClientNextPage() {
  restaurantCurrentPage++;
  showRestaurantsClient();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Renders the restaurant form initialization HTML.
 * @returns {string} - The HTML string for the restaurant form initialization.
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
                        <option value="nome">Nome</option>
                        <option value="morada">Morada</option>
                      </select>
                    </div>
                      <button type="button" id="submit-filter-button" onclick="submitRestaurantSearch()">Procurar</button>
                      <button type="button" id="reset-filter-button" onclick="resetRestaurantSearch()">Reset</button>
                  </form>
                </div>
              <ul id="generic-card-list">`);
}

// -------------------------------------------------- OFFERS --------------------------------------------------

/**
 * Fetch and display offer list for the client.
 * Sends a POST request to the client offer list endpoint and renders the received data.
 * Updates the content div with the list of offers and pagination controls.
 */
function showOffersClient() {
  restaurantCurrentPage = 0; // Reset restaurant page

  xhr.open("POST", clientOfferLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  let data = `page=${offersCurrentPage}`;

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const offers = JSON.parse(this.responseText);

      let output = offerFormInit();

      offers.oferta_set.forEach(function (offer) {
        output += renderClientOfferCard(offer);
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
                        <button onclick="offersClientPrevPage()" id="offers-prev-page" ${isPrevPage}>
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
                        <button onclick="offersClientNextPage()" id="offers-next-page" ${isNextPage}>
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
 * Renders an offer card for the client view.
 * @param {Object} offer, that contains offer details.
 * @returns {string} HTML string representing the offer card.
 */
function renderClientOfferCard(offer) {
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
          <div class="reservation-form-container" id="reservation-form-container-${offer_id}">
            <form class="reservation-form" onsubmit="return false;">
              <label for="units_reserve">Unidades a reservar:</label>
              <div class="form-group">
                <input type="number" id="units_reserve" name="unidades" value="1" min="1" max="${units}" required>
                <button type="button" class="reserve-button" onclick="reserveOffer(${offer_id})">Reservar</button>
              </div>
            </form>
          </div>
        </li>`;
}


/**
 * Resets the current page and search page to 0 and displays the full offer list.
 */
function resetOfferSearch() {
  offersCurrentPage = 0;
  offersSearchPage = 0;
  showOffersClient();
}


/**
 * Submits the offer search form.
 * Sends a POST request to the client offer search endpoint with the current filter.
 * Updates the content div with the search results and pagination controls.
 * @param {boolean} fromPagination, indicates if the submission is from pagination controls.
 */
function submitOfferSearch(fromPagination = false) {
  xhr.open("POST", clientOfferSearchLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  // If not from pagination (next or previous page), update prevFilter and reset page
  if (!fromPagination) {
    let currentType = document.getElementById("filter-type").value;
    let currentVal = document.getElementById("toFilter").value;

    updatePrevFilter(currentType, currentVal);
    offersSearchPage = 0;
  }
  
  // Encode toFilter to handle special characters
  let encodedToFilter = encodeURIComponent(prevFilter.toFilter);
  let data = `${prevFilter.filterType}=${encodedToFilter}&page=${offersSearchPage}`;

  
  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const offers = JSON.parse(this.responseText);

      let output = offerFormInit();
      offers.oferta_set.forEach(function (offer) {
        output += renderClientOfferCard(offer);
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
                    <button onclick="offersSearchPrevPage()" id="offers-prev-page" ${isPrevPage}>
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
                    <span id="page-info">Página <strong>${(offers.page + 1) | 0}</strong> de ${(offers.last_page + 1) | 0}</span>
                    <button onclick="offersSearchNextPage()" id="offers-next-page" ${isNextPage}>
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

      document.getElementById("toFilter").value = prevFilter.toFilter;
      document.getElementById("filter-type").value = prevFilter.filterType;
    }
  };
  xhr.send(data);
}


/**
 * Handles pagination for the full offer list.
 * Increments the current page and displays the offer list.
 * Scrolls to the top of the content area for better user experience.
 */
function offersClientNextPage() {
  offersCurrentPage += 1;
  showOffersClient();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for the full offer list.
 * Decrements the current page and displays the offer list.
 * Scrolls to the top of the content area for better user experience.
 */
function offersClientPrevPage() {
  offersCurrentPage -= 1;
  showOffersClient();
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for offer search results.
 * Increments the search page and submits the search form.
 * Scrolls to the top of the content area for better user experience.
 */
function offersSearchNextPage() {
  offersSearchPage += 1;
  submitOfferSearch(true);
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Handles pagination for offer search results.
 * Decrements the search page and submits the search form.
 * Scrolls to the top of the content area for better user experience.
 */
function offersSearchPrevPage() {
  offersSearchPage -= 1;
  submitOfferSearch(true);
  window.scrollTo({ top: 50, behavior: "smooth" });
}


/**
 * Renders the offer form initialization HTML.
 * @returns {string} - The HTML string for the offer form initialization.
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
                  <option value="nome">Nome</option>
                  <option value="restaurante_id">Restaurante ID</option>
                </select>
              </div>
                <button type="button" id="submit-filter-button" onclick="submitOfferSearch()">Procurar</button>
                <button type="button" id="reset-filter-button" onclick="resetOfferSearch()">Reset</button>
            </form>
          </div>
          <ul id="offer-list">`;
  return output;
}


/**
 * Reserves an offer for a random client.
 * Sends a POST request to the client reserve offer endpoint with the offer ID, client ID and units to reserve.
 * Handles the response to show success or error messages.
 * @param {number} offerID - The ID of the offer to reserve.
 */
function reserveOffer(offerID) {
  const units = document.getElementById("units_reserve").value;
  const data = new URLSearchParams({
    oferta_id: offerID,
    unidades: units,
    //simulating a logged in user with random id
    cliente_id: Math.floor(Math.random() * 10000) + 1,
  }).toString();

  xhr.open("POST", clientReserveOffer, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const response = JSON.parse(this.responseText);

      if (response.status === "ok") {
        showReservationSuccessMessage(response.reserva_id);
      } else {
        showReservationErrorMessage(
          response.status || "Erro desconhecido",
          offerID
        );
      }
    }
  };

  xhr.send(data);
}


/**
 * Displays a success message for a successful reservation.
 */
function showReservationSuccessMessage(reservaId) {
  content.innerHTML = `
          <div id="add-offer-container">
            <div class="success-card">
              <div class="success-header">
                <h3>Reserva realizada com Sucesso!</h3>
              </div>
              <span class="success-id">ID ${reservaId}</span>
              <button 
                id="add-offer-button" 
                onclick="showOffersClient()"
              >
                Voltar às Ofertas
              </button>
            </div>
          </div>
        `;
}


/**
 * Displays an error message for reservation failures.
 * The message is displayed below the reservation form of the specific offer.
 * @param {string} message - The error message to display.
 * @param {number} offer_id - The ID of the offer related to the error.
 */
function showReservationErrorMessage(message, offer_id) {
  // Remove any existing error message
  const existingError = document.querySelector(".submit-error");
  if (existingError) {
    existingError.remove();
  }

  // Create error message
  const errorDiv = document.createElement("div");
  errorDiv.className = "submit-error";
  errorDiv.textContent = message;

  // Insert after the submit button
  const formContainer = document.getElementById(
    `reservation-form-container-${offer_id}`
  );
  formContainer.parentElement.appendChild(errorDiv);
}


// -------------------------------------------------- UTILITIES --------------------------------------------------


/**
 * Handles image loading errors by setting a default placeholder image.
 * @param {HTMLImageElement} image 
 */
function errorLoadingImage(image) {
  image.onerror = null; // Avoiding an infinite loop if the placeholder also fails
  image.src = "../assets/img/default-offer-image-placeholder.png";
}


/**
 * Updates the previous filter state.
 * @param {string} filterType - The type of filter applied e.g. nome, morada
 * @param {string} toFilter - The value to filter by.
 */
function updatePrevFilter(filterType, toFilter) {
  prevFilter.filterType = filterType;
  prevFilter.toFilter = toFilter;
}
