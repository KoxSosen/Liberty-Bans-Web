let ready = (callback) => {
    if (document.readyState !== "loading") callback();
    else document.addEventListener("DOMContentLoaded", callback);
};

ready(() => {
    let currentPage = 1;
    let currentType = 'ban';
    let morePages = true;

    function fetchTypeStats(type) {
        const typeStats = document.querySelector("#type-stats");
        let typeText = "Bans";

        fetch(`/stats/${type}`)
            .then(data => {
                switch (type) {
                    case "ban":
                        typeText = "Bans";
                        break;

                    case "mute":
                        typeText = "Mutes";
                        break;

                    case "kick":
                        typeText = "Kicks";
                        break;

                    case "warn":
                        typeText = "Warns";
                        break;
                }

                let text = document.createTextNode(`${typeText} <span class="fs-2">(${data.stats})</span>`);
                typeStats.appendChild(text);

            }).catch(error => {
                console.error("Error retrieving type stats");
                console.error(error);
        });
    }

    function updatePageCount() {
        const pageCount = document.querySelector("#pageCount");
        pageCount.value = currentPage;
    }

    function removeActiveClass() {
        document.querySelector('#banType').parentElement.classList.remove("navbar-active");
        document.querySelector('#kickType').parentElement.classList.remove('navbar-active');
        document.querySelector('#muteType').parentElement.classList.remove('navbar-active');
        document.querySelector('#warnType').parentElement.classList.remove('navbar-active');
    }

    fetchPunishments(currentType, currentPage);

    document.querySelector('#nextBtn').addEventListener('click', () => {
        currentPage++;
        fetchPunishments(currentType, currentPage);
    });

    document.querySelector('#prevBtn').addEventListener('click', () => {
        currentPage--;
        fetchPunishments(currentType, currentPage);
    });

    document.querySelector('#banType').addEventListener('click', () => {
        currentPage = 1;
        currentType = 'ban';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        document.querySelector('#banType').parentElement.add('navbar-active');
    });

    document.querySelector('#kickType').addEventListener('click', () => {
        currentPage = 1;
        currentType = 'kick';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        document.querySelector('#kickType').parentElement.add('navbar-active');
    });

    document.querySelector('#muteType').addEventListener('click', () => {
        currentPage = 1;
        currentType = 'mute';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        document.querySelector('#muteType').parentElement.add('navbar-active');
    });

    document.querySelector('#warnType').addEventListener('click', () => {
        currentPage = 1;
        currentType = 'warn';
        fetchPunishments(currentType, currentPage);
        removeActiveClass();
        document.querySelector('#warnType').parent().add('navbar-active');
    });

    document.querySelector('#pageCount').addEventListener('keyup', function() {
            let pageCountValue = Number(document.querySelector("#pageCount"));

            if (isNaN(pageCountValue)) {
                return false;
            }

            // The previous call should ensure that the pageCountValue variable is a number
            // but leaving this here for safety.
            if (typeof pageCountValue != 'number') {
                return false;
            }

            if (pageCountValue < 1) {
                return false;
            }

            if (!morePages && pageCountValue > currentPage) {
                return false;
            }

            if (pageCountValue === currentPage) {
                return false;
            }

            currentPage = pageCountValue;
            fetchPunishments(currentType, currentPage);
            return false;
    });

    function fetchPunishments(type, page) {
        const spinner = document.querySelector("#punishments-spinner");
        const punishmentsElement = document.querySelector("#punishments");

        // Hide punishments using CSS.
        punishmentsElement.style.display = "none";
        spinner.style.display = "block";

        fetch("/punishments/" + type + "/" + page)
            .then((response) => response.json())
            .then((data) => console.log(data))
            .then((data) => {
                fetchTypeStats(type);
                updatePageCount();

                // .empty() equivalent
                punishmentsElement.textContent = '';

                if (!data.ok) {
                    morePages = data.morePages;

                    for (const punishment of data.punishments) {
                        let statusBadge;
                        let line;
                        let operatorUuid = punishment.operatorUuid;

                        if (punishment.label === "Permanent") {
                            line = '<div class="col-auto permanent-line"></div>';
                            statusBadge = '<span class="permanent fw-medium">Permanent</span>';
                        } else if (punishment.label === "Active") {
                            line = '<div class="col-auto active-line"></div>';
                            statusBadge = '<span class="active fw-medium">Active</span>';
                        } else {
                            line = '<div class="col-auto expired-line"></div>';
                            statusBadge = '<span class="expired fw-medium">Expired</span>';
                        }

                        if (operatorUuid === '00000000-0000-0000-0000-000000000000') {
                            operatorUuid = "console";
                        }

                        const html = `
                        <div class="row align-items-center p-3 flex-nowrap">
                        ${line}
                        <div class="col-auto">
                        <img src="https://visage.surgeplay.com/face/55/${punishment.victimUuid}" alt="punishment-victimUUID">
                        </div>
                        <div class="col">
                        <p class="fs-5 mb-0">Offender</p>
                        <p>${punishment.victimUsername}</p>
                        </div>
                        <div class="col-auto">
                        <img src="https://visage.surgeplay.com/face/55/${operatorUuid}" alt="punishment-operatorUUID">
                        </div>
                        <div class="col">
                        <p class="fs-5 mb-0">Staff</p>
                        <p>${punishment.operatorUsername}</p>
                        </div>
                        <div class="col">
                        <div class="fs-5 mb-0">Reason</div>
                        <p>${punishment.reason}</p>
                        </div>
                        <div class="col">
                        ${statusBadge}
                        </div>
                        ${line}
                        </div>
                        `;
                        punishmentsElement.append(html);

                        spinner.style.display = "none";
                        punishmentsElement.style.display = "block";
                    }
                } else {
                    const html = `<div class="text-center p-5"> <p class="fw-medium fs-5 mb-0">Nothing to show for now</p></div>`;
                    punishmentsElement.append(html);
                }
                if (!morePages) {
                    document.querySelector('#nextBtn').setProperty('disabled', true);
                    document.querySelector('#nextBtn').classList.add('disabled-btn');
                } else {
                    document.querySelector('#nextBtn').setProperty('disabled', false);
                    document.querySelector('#nextBtn').classList.remove('disabled-btn');
                }

                if (currentPage <= 1) {
                    document.querySelector('#prevBtn').setProperty('disabled', true);
                    document.querySelector('#prevBtn').classList.add('disabled-btn');
                } else {
                    document.querySelector('#prevBtn').setProperty('disabled', false);
                    document.querySelector('#prevBtn').classList.remove('disabled-btn');
                }
            }).catch(error => {
                console.log(error);
                console.log("Error retrieving punishment history");
        });
    }
});