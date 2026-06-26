const API = 'http://localhost:8080';
const NOTAS = [2, 5, 10, 20, 50, 100, 200];

const CARDAPIO = [
    {sabor: 'frango', emoji: '<img src="/images/coxinha.png" class="coxinha-img">', preco: 8.0},
    {sabor: 'carne', emoji: '<img src="/images/coxinha.png" class="coxinha-img">', preco: 10.0},
    {sabor: 'costela', emoji: '<img src="/images/coxinha.png" class="coxinha-img">', preco: 10.0},
    {sabor: 'calabresa', emoji: '<img src="/images/coxinha.png" class="coxinha-img">', preco: 12.0},
    {sabor: 'palmito', emoji: '<img src="/images/coxinha.png" class="coxinha-img">', preco: 15.0}
];

let clienteLogado = null;
let notaSelecionada = null;
let timeoutEstorno = null;

function toast(msg, tipo = 'info') {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = `show ${tipo}`;
    setTimeout(() => {
        t.className = '';
    }, 3000);
}

function formatarMoeda(valor) {
    return parseFloat(valor).toLocaleString('pt-BR', {minimumFractionDigits: 2});
}

function formatarData(dataISO) {
    if (!dataISO) return '—';
    const d = new Date(dataISO);
    return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', {hour: '2-digit', minute: '2-digit'});
}

async function atualizarSaldoExibido() {
    if (!clienteLogado) return;
    try {
        const res = await fetch(`${API}/clientes/${clienteLogado.id}/extrato`);
        const data = await res.json();
        const saldo = data.saldo || 0;
        clienteLogado.saldo = saldo;
        document.getElementById('saldo-valor').innerText = formatarMoeda(saldo);
        if (document.getElementById('extrato-saldo')) {
            document.getElementById('extrato-saldo').innerText = formatarMoeda(saldo);
        }
    } catch (err) {
        console.error('Erro ao atualizar saldo', err);
    }
}

async function carregarClientes() {
    try {
        const res = await fetch(`${API}/clientes`);
        const clientes = await res.json();
        const sel = document.getElementById('select-cliente');
        if (!clientes.length) {
            sel.innerHTML = '<option value="">Nenhum cliente cadastrado</option>';
            return;
        }
        sel.innerHTML = '<option value="">Selecione...</option>' +
            clientes.map(c => `<option value="${c.id}" data-saldo="${c.saldo}">${c.nome}</option>`).join('');
    } catch (err) {
        document.getElementById('select-cliente').innerHTML = '<option value="">Erro ao carregar</option>';
        toast('Erro ao conectar com o servidor', 'error');
    }
}

function switchTab(tab) {
    const isLogin = tab === 'login';
    document.getElementById('form-login').style.display = isLogin ? 'block' : 'none';
    document.getElementById('form-cadastro').style.display = isLogin ? 'none' : 'block';
    const btnLogin = document.getElementById('tab-login');
    const btnCad = document.getElementById('tab-cadastro');
    if (isLogin) {
        btnLogin.classList.add('active');
        btnCad.classList.remove('active');
    } else {
        btnCad.classList.add('active');
        btnLogin.classList.remove('active');
    }
    document.getElementById('input-senha').value = '';
    document.getElementById('input-senha-cadastro').value = '';
    document.getElementById('input-nome').value = '';
}

async function cadastrarCliente() {
    const nome = document.getElementById('input-nome').value.trim();
    const senha = document.getElementById('input-senha-cadastro').value.trim();
    if (!nome) {
        toast('Digite seu nome!', 'error');
        return;
    }
    if (!senha) {
        toast('Crie uma senha!', 'error');
        return;
    }
    try {
        const res = await fetch(`${API}/clientes`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ nome, senha, saldo: 0 })
        });
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.erro || 'Erro ao cadastrar');
        }
        toast('Conta criada com sucesso!', 'success');
        document.getElementById('input-nome').value = '';
        document.getElementById('input-senha-cadastro').value = '';
        switchTab('login');
        await carregarClientes();
    } catch (err) {
        toast(err.message, 'error');
    }
}

async function fazerLogin() {
    const sel = document.getElementById('select-cliente');
    const opt = sel.options[sel.selectedIndex];
    if (!opt || !opt.value) {
        toast('Selecione um cliente!', 'error');
        return;
    }
    const senha = document.getElementById('input-senha').value.trim();
    if (!senha) {
        toast('Digite sua senha!', 'error');
        return;
    }
    try {
        const res = await fetch(`${API}/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                clienteId: parseInt(opt.value),
                senha: senha
            })
        });
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(errData.erro || 'Erro no login');
        }
        const cliente = await res.json();
        clienteLogado = {
            id: cliente.id,
            nome: cliente.nome,
            saldo: cliente.saldo || 0
        };
        document.getElementById('page-login').classList.remove('active');
        document.getElementById('navbar').style.display = 'flex';
        document.getElementById('input-senha').value = '';
        document.getElementById('input-senha-cadastro').value = '';
        document.getElementById('input-nome').value = '';
        atualizarSaldoExibido();
        renderCardapio();
        renderNotas();
        carregarSlots();
        showPage('home');
        toast(`Bem-vindo(a), ${clienteLogado.nome}!`, 'success');
    } catch (err) {
        toast(err.message, 'error');
    }
}

function logout() {
    clienteLogado = null;
    notaSelecionada = null;
    document.getElementById('navbar').style.display = 'none';
    document.getElementById('page-login').classList.add('active');
    document.getElementById('page-home').classList.remove('active');
    document.getElementById('page-extrato').classList.remove('active');
    document.getElementById('page-slots').classList.remove('active');
    document.getElementById('input-senha').value = '';
    document.getElementById('input-senha-cadastro').value = '';
    document.getElementById('input-nome').value = '';
    carregarClientes();
}

function showPage(pageName) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(`page-${pageName}`).classList.add('active');
    if (pageName === 'extrato') carregarExtrato();
    if (pageName === 'slots') carregarSlots();
    document.querySelectorAll('.nav-links a').forEach(a => a.classList.remove('active'));
    const activeLink = document.getElementById(`nav-${pageName}`);
    if (activeLink) activeLink.classList.add('active');
}

function renderNotas() {
    const grid = document.getElementById('notas-grid');
    if (!grid) return;
    grid.innerHTML = NOTAS.map(n => `
        <button class="nota-btn" onclick="selecionarNota(${n}, this)">R$ ${n}</button>
    `).join('');
}

function selecionarNota(valor, btn) {
    notaSelecionada = valor;
    document.querySelectorAll('.nota-btn').forEach(b => b.classList.remove('selected'));
    btn.classList.add('selected');
    document.getElementById('nota-selecionada').innerText = `R$ ${valor}`;
}

async function inserirCredito() {
    if (!clienteLogado) {
        toast('Faça login primeiro', 'error');
        return;
    }
    if (!notaSelecionada) {
        toast('Selecione uma nota!', 'error');
        return;
    }
    try {
        const res = await fetch(`${API}/credito`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                clienteId: clienteLogado.id,
                valorNota: notaSelecionada
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.mensagem || 'Erro ao inserir crédito');
        toast(data.mensagem, 'success');
        notaSelecionada = null;
        document.querySelectorAll('.nota-btn').forEach(b => b.classList.remove('selected'));
        document.getElementById('nota-selecionada').innerText = '—';
        await atualizarSaldoExibido();
    } catch (err) {
        toast(err.message, 'error');
    }
}

function atualizarPrecoFrango() {
    const checkbox = document.getElementById('recheio-frango');
    const isChecked = checkbox ? checkbox.checked : false;
    const nomeEl = document.getElementById('nome-frango');
    const precoEl = document.getElementById('preco-frango');
    if (nomeEl && precoEl) {
        nomeEl.innerText = isChecked ? 'Frango Especial' : 'Frango';
        const preco = isChecked ? 10.0 : 8.0;
        precoEl.innerText = `R$ ${formatarMoeda(preco)}`;
    }
}

function atualizarPrecoCarne() {
    const checkbox = document.getElementById('desconto-carne');
    const isChecked = checkbox ? checkbox.checked : false;
    const nomeEl = document.getElementById('nome-carne');
    const precoEl = document.getElementById('preco-carne');
    if (nomeEl && precoEl) {
        nomeEl.innerText = isChecked ? 'Carne (com 20% de desconto)' : 'Carne';
        const preco = isChecked ? 8.0 : 10.0;
        precoEl.innerText = `R$ ${formatarMoeda(preco)}`;
    }
}

async function comprarCoxinha(sabor, preco) {
    if (!clienteLogado) {
        toast('Faça login', 'error');
        return;
    }

    let adicionarRecheio = false;
    let aplicarDesconto = false;

    if (sabor === 'frango') {
        const checkbox = document.getElementById('recheio-frango');
        adicionarRecheio = checkbox ? checkbox.checked : false;
    }
    if (sabor === 'carne') {
        const checkbox = document.getElementById('desconto-carne');
        aplicarDesconto = checkbox ? checkbox.checked : false;
    }

    const precoEfetivo = (sabor === 'frango' && adicionarRecheio) ? 10.0 :
        (sabor === 'carne' && aplicarDesconto) ? 8.0 :
            preco;

    if (clienteLogado.saldo < precoEfetivo) {
        toast(`Saldo insuficiente! Seu saldo: R$ ${formatarMoeda(clienteLogado.saldo)}`, 'error');
        return;
    }

    try {
        const res = await fetch(`${API}/compras`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                clienteId: clienteLogado.id,
                sabor: sabor,
                adicionarRecheio: adicionarRecheio,
                aplicarDesconto: aplicarDesconto
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.mensagem || 'Erro na compra');

        let msg = `${data.mensagem} | Troco: R$ ${formatarMoeda(data.troco)}`;
        if (data.notasTroco && Object.keys(data.notasTroco).length > 0) {
            const notasStr = Object.entries(data.notasTroco)
                .map(([nota, qtd]) => `${qtd}x R$${nota}`)
                .join(', ');
            msg += ` | Notas: ${notasStr}`;
        }
        toast(msg, 'success');

        await atualizarSaldoExibido();
        await carregarExtrato();
    } catch (err) {
        toast(err.message, 'error');
    }
}

function renderCardapio() {
    const grid = document.getElementById('cardapio-grid');
    if (!grid) return;

    grid.innerHTML = CARDAPIO.map((item, i) => {
        if (item.sabor === 'frango') {
            return `
                <div class="coxinha-card fade-in" style="animation-delay:${i * 0.05}s">
                    <div class="coxinha-emoji">${item.emoji}</div>
                    <h3 id="nome-frango">Frango</h3>
                    <div class="coxinha-price" id="preco-frango">R$ ${formatarMoeda(8.0)}</div>
                    <label style="display:block; margin-top: 10px; font-size: 0.85rem; cursor:pointer;">
                        <input type="checkbox" id="recheio-frango" onchange="atualizarPrecoFrango()">
                        Adicionar recheio (+R$2,00)
                    </label>
                    <div style="margin-top: 10px;">
                        <button class="btn-comprar" onclick="comprarCoxinha('frango', 8.0)">Comprar</button>
                    </div>
                </div>
            `;
        } else if (item.sabor === 'carne') {
            return `
                <div class="coxinha-card fade-in" style="animation-delay:${i * 0.05}s">
                    <div class="coxinha-emoji">${item.emoji}</div>
                    <h3 id="nome-carne">Carne</h3>
                    <div class="coxinha-price" id="preco-carne">R$ ${formatarMoeda(10.0)}</div>
                    <label style="display:block; margin-top: 10px; font-size: 0.85rem; cursor:pointer;">
                        <input type="checkbox" id="desconto-carne" onchange="atualizarPrecoCarne()">
                        Aplicar desconto (20%)
                    </label>
                    <div style="margin-top: 10px;">
                        <button class="btn-comprar" onclick="comprarCoxinha('carne', 10.0)">Comprar</button>
                    </div>
                </div>
            `;
        } else {
            return `
                <div class="coxinha-card fade-in" style="animation-delay:${i * 0.05}s" onclick="comprarCoxinha('${item.sabor}', ${item.preco})">
                    <div class="coxinha-emoji">${item.emoji}</div>
                    <h3>${item.sabor}</h3>
                    <div class="coxinha-price">R$ ${formatarMoeda(item.preco)}</div>
                </div>
            `;
        }
    }).join('');
}

function popularSelectsTroca() {
    const selectOrigem = document.getElementById('sabor-origem');
    const selectDestino = document.getElementById('sabor-destino');
    if (!selectOrigem) return;

    const sabores = ['frango', 'carne', 'costela', 'frango especial', 'carne especial', 'calabresa', 'palmito'];
    const opcoes = sabores.map(s => `<option value="${s}">${s}</option>`).join('');
    selectOrigem.innerHTML = '<option value="">Sabor original</option>' + opcoes;
    selectDestino.innerHTML = '<option value="">Novo sabor</option>' + opcoes;
}

async function trocarSabor() {
    const origem = document.getElementById('sabor-origem').value;
    const destino = document.getElementById('sabor-destino').value;
    if (!origem || !destino) {
        toast('Selecione ambos os sabores', 'error');
        return;
    }
    if (!clienteLogado) {
        toast('Faça login', 'error');
        return;
    }
    try {
        const res = await fetch(`${API}/trocar-sabor`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                clienteId: clienteLogado.id,
                saborOrigem: origem,
                saborDestino: destino
            })
        });
        const data = await res.json();
        if (!res.ok) throw new Error(data.mensagem || 'Erro na troca');
        toast(data.mensagem, 'success');
        await atualizarSaldoExibido();
        await carregarExtrato();
    } catch (err) {
        toast(err.message, 'error');
    }
}

async function estornarCompra(movimentacaoId, botao) {
    if (!clienteLogado) {
        toast('Faça login', 'error');
        return;
    }

    if (botao.classList.contains('confirmando')) {
        if (timeoutEstorno) {
            clearTimeout(timeoutEstorno);
            timeoutEstorno = null;
        }

        try {
            const res = await fetch(`${API}/estorno/${movimentacaoId}`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'}
            });
            const data = await res.json();
            if (!res.ok) throw new Error(data.mensagem || 'Erro ao estornar');
            toast(data.mensagem, 'success');
            await atualizarSaldoExibido();
            await carregarExtrato();
        } catch (err) {
            toast(err.message, 'error');
        }
        return;
    }

    const textoOriginal = botao.textContent;
    botao.textContent = 'Confirmar?';
    botao.classList.add('confirmando');
    botao.style.background = '#C0392B';
    botao.style.color = 'white';
    botao.style.border = '2px solid #922B21';

    if (timeoutEstorno) clearTimeout(timeoutEstorno);
    timeoutEstorno = setTimeout(() => {
        botao.textContent = textoOriginal;
        botao.classList.remove('confirmando');
        botao.style.background = '';
        botao.style.color = '';
        botao.style.border = '';
        timeoutEstorno = null;
    }, 5000);
}

async function carregarExtrato() {
    if (!clienteLogado) return;
    try {
        const res = await fetch(`${API}/clientes/${clienteLogado.id}/extrato`);
        const data = await res.json();
        document.getElementById('extrato-saldo').innerText = formatarMoeda(data.saldo || 0);
        document.getElementById('extrato-sub').innerHTML = `Olá, ${data.cliente}! Aqui está seu histórico completo: `;
        const tbody = document.getElementById('extrato-tbody');
        const movs = data.movimentacoes || [];
        if (movs.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5"><div class="empty-state">Nenhuma movimentação ainda...</div></td></tr>`;
            return;
        }
        tbody.innerHTML = movs.slice().reverse().map(m => `
    <tr>
        <td>${formatarData(m.dataHora)}</td>
        <td>${m.sabor || '—'}</td>
        <td><span class="badge ${m.tipoOperacao === 'COMPRA' ? 'badge-compra' : m.tipoOperacao === 'ESTORNO' ? 'badge-estorno' : 'badge-troca'}">${m.tipoOperacao}</span></td>
        <td>R$ ${formatarMoeda(m.valor)}</td>
        <td>
            ${m.tipoOperacao === 'COMPRA' && !m.estornado ? `
                <button class="btn-estornar" onclick="estornarCompra(${m.id}, this)">↩ Estornar</button>
            ` : ''}
        </td>
    </tr>
`).join('');
    } catch (err) {
        toast('Erro ao carregar extrato', 'error');
    }
}

async function carregarSlots() {
    try {
        const res = await fetch(`${API}/slots`);
        const slots = await res.json();
        const grid = document.getElementById('slots-grid');
        if (!slots.length) {
            grid.innerHTML = '<div>Nenhum slot configurado</div>';
            return;
        }
        const ordenados = slots.sort((a, b) => a.valorNota - b.valorNota);
        grid.innerHTML = ordenados.map(s => `
            <div class="slot-card">
                <div class="slot-valor">R$ ${s.valorNota}</div>
                <div class="slot-qtd">${s.qtd} unidades</div>
            </div>
        `).join('');
    } catch (err) {
        document.getElementById('slots-grid').innerHTML = '<div>Erro ao carregar slots</div>';
    }
}

window.addEventListener('DOMContentLoaded', () => {
    carregarClientes();
    popularSelectsTroca();
    setInterval(() => {
        if (clienteLogado) atualizarSaldoExibido();
    }, 10000);
});