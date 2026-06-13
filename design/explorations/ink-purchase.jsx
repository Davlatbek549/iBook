/* DZ Ink & Paper — purchase flow screens.
   Exposes window.DZInkPurchase = { PrePurchase, PurchaseConfirmation, PurchaseDetails, PaymentMethods, PaymentSuccess, PaymentFailed } */
(function () {
  const { Ic, pal, cardStyle, Frame, Label, IconBtn, TopBar, Btn, BOOKS } = window.DZInk;

  const PriceRow = ({ t, c, label, val, strong, accent }) => (
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'baseline' }}>
      <span style={{ font: `${strong ? 700 : 400} 13px/1 ${t.fontBody}`, color: strong ? c.ink : c.muted }}>{label}</span>
      <span style={{ font: strong ? `${t.displayWeight} 19px/1 ${t.fontDisplay}` : `600 13px/1 ${t.fontBody}`, color: accent ? c.accent : c.ink }}>{val}</span>
    </div>
  );

  const PayPalRow = ({ t, c, selected, trailing }) => (
    <div style={{ display: 'flex', alignItems: 'center', gap: 13 }}>
      <div style={{ width: 42, height: 42, borderRadius: t.radiusSm, background: c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="paypal" s={20} /></div>
      <div style={{ flex: 1 }}>
        <div style={{ font: `700 13.5px/1 ${t.fontBody}`, color: c.ink }}>PayPal</div>
        <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>amelia@hartwell.co</div>
      </div>
      {trailing}
    </div>
  );

  const Radio = ({ c, on }) => (
    <span style={{ width: 20, height: 20, borderRadius: 999, border: `2px solid ${on ? c.accent : c.line}`, display: 'inline-flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
      {on && <span style={{ width: 9, height: 9, borderRadius: 999, background: c.accent }} />}
    </span>
  );

  const CardGlyph = ({ c }) => (
    <svg width="22" height="16" viewBox="0 0 22 16" fill="none"><rect x="0.75" y="0.75" width="20.5" height="14.5" rx="3" stroke={c.ink} strokeWidth="1.5"/><rect x="0.75" y="4.4" width="20.5" height="2.6" fill={c.ink}/><rect x="3.4" y="10.4" width="6" height="1.8" rx="0.9" fill={c.ink}/></svg>
  );

  /* ---------- Pre-purchase review ---------- */
  function PrePurchase({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const b = BOOKS[0];
    return (
      <Frame t={t} c={c} h={820}>
        <div style={{ height: '100%', position: 'relative', paddingBottom: 96 }}>
          <TopBar t={t} c={c} title="Review your order" />

          <div style={{ margin: '8px 22px 0', ...cardStyle(t, c), padding: 16, display: 'flex', gap: 15, alignItems: 'center' }}>
            <img src={b.c} alt="" style={{ width: 58, height: 86, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 8px 16px rgba(20,18,12,0.2)' }} />
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ font: `${t.displayWeight} 16px/1.25 ${t.fontDisplay}`, color: c.ink }}>{b.t}</div>
              <div style={{ font: `400 12px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{b.a}</div>
              <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 8 }}>Ebook · yours forever</div>
            </div>
            <span style={{ font: `${t.displayWeight} 16px/1 ${t.fontDisplay}`, color: c.ink }}>${b.price}</span>
          </div>

          {/* coins */}
          <div style={{ margin: '14px 22px 0', ...cardStyle(t, c), background: c.alt, border: 'none', padding: '14px 16px', display: 'flex', alignItems: 'center', gap: 12 }}>
            <Ic n="tag" s={17} color={c.accent} />
            <span style={{ flex: 1, font: `400 12.5px/1.4 ${t.fontBody}`, color: c.inkSoft }}>Use <b style={{ color: c.ink, fontWeight: 700 }}>240 coins</b> for $2.40 off</span>
            <span style={{ font: `700 12px/1 ${t.fontBody}`, color: c.accent }}>Apply</span>
          </div>

          {/* payment method */}
          <div style={{ padding: '22px 22px 0' }}>
            <Label t={t} c={c}>Paying with</Label>
            <div style={{ ...cardStyle(t, c), padding: '13px 16px', marginTop: 11 }}>
              <PayPalRow t={t} c={c} trailing={<span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>Change</span>} />
            </div>
          </div>

          {/* summary */}
          <div style={{ padding: '22px 22px 0' }}>
            <Label t={t} c={c}>Summary</Label>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 13 }}>
              <PriceRow t={t} c={c} label="Book price" val="$12.99" />
              <PriceRow t={t} c={c} label="Coins applied" val="−$2.40" accent />
              <PriceRow t={t} c={c} label="Tax" val="$0.84" />
              <div style={{ height: 1, background: c.line, margin: '2px 0' }} />
              <PriceRow t={t} c={c} label="Total" val="$11.43" strong />
            </div>
          </div>

          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, padding: '14px 22px 18px', background: c.paper, borderTop: `1px solid ${c.line}` }}>
            <Btn t={t} c={c}>Confirm purchase · $11.43</Btn>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Purchase confirmation (sheet) ---------- */
  function PurchaseConfirmation({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const b = BOOKS[0];
    return (
      <Frame t={t} c={c} h={760}>
        <div style={{ height: '100%', position: 'relative' }}>
          {/* dimmed page behind */}
          <div style={{ padding: '4px 22px', opacity: 0.35, filter: 'blur(1px)' }}>
            <TopBar t={t} c={c} title="Review your order" />
            <div style={{ ...cardStyle(t, c), padding: 16, display: 'flex', gap: 15 }}>
              <img src={b.c} alt="" style={{ width: 58, height: 86, borderRadius: t.cover, objectFit: 'cover' }} />
              <div>
                <div style={{ font: `${t.displayWeight} 16px/1.25 ${t.fontDisplay}`, color: c.ink }}>{b.t}</div>
                <div style={{ font: `400 12px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{b.a}</div>
              </div>
            </div>
          </div>
          <div style={{ position: 'absolute', inset: 0, background: mode === 'D' ? 'rgba(0,0,0,0.5)' : 'rgba(33,28,22,0.35)' }} />

          {/* sheet */}
          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, background: c.paper, borderRadius: '24px 24px 0 0', padding: '14px 24px 26px', boxShadow: '0 -20px 50px rgba(20,18,12,0.3)' }}>
            <div style={{ width: 36, height: 4, borderRadius: 999, background: c.line, margin: '0 auto 20px' }} />
            <div style={{ font: `${t.displayWeight} 23px/1.15 ${t.fontDisplay}`, color: c.ink, textAlign: 'center' }}>Confirm this purchase?</div>
            <p style={{ font: `400 13px/1.6 ${t.fontBody}`, color: c.muted, textAlign: 'center', margin: '10px 0 0' }}>PayPal will charge $11.43. The book is added to your library immediately.</p>

            <div style={{ ...cardStyle(t, c), padding: 14, margin: '18px 0 0', display: 'flex', gap: 13, alignItems: 'center' }}>
              <img src={b.c} alt="" style={{ width: 42, height: 62, borderRadius: t.cover - 1, objectFit: 'cover' }} />
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ font: `${t.displayWeight} 14.5px/1.2 ${t.fontDisplay}`, color: c.ink }}>{b.t}</div>
                <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{b.a}</div>
              </div>
              <span style={{ font: `${t.displayWeight} 17px/1 ${t.fontDisplay}`, color: c.ink }}>$11.43</span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: 10, marginTop: 18 }}>
              <Btn t={t} c={c}>Yes, buy it</Btn>
              <Btn t={t} c={c} kind="secondary">Not yet</Btn>
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Purchase details (receipt) ---------- */
  function PurchaseDetails({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const b = BOOKS[0];
    return (
      <Frame t={t} c={c} h={820}>
        <div style={{ height: '100%', position: 'relative', paddingBottom: 96 }}>
          <TopBar t={t} c={c} title="Purchase details" right={<IconBtn t={t} c={c} n="share" s={16} />} />

          <div style={{ margin: '8px 22px 0', ...cardStyle(t, c), padding: 18 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Label t={t} c={c}>Order DZ-20614</Label>
              <span style={{ display: 'inline-flex', alignItems: 'center', gap: 6, height: 24, padding: '0 11px', borderRadius: 999, background: c.accentSoft, font: `700 10.5px/1 ${t.fontBody}`, color: c.accent }}>
                <Ic n="done" s={9} />Completed
              </span>
            </div>
            <div style={{ display: 'flex', gap: 15, alignItems: 'center', marginTop: 16 }}>
              <img src={b.c} alt="" style={{ width: 54, height: 80, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 8px 16px rgba(20,18,12,0.2)' }} />
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ font: `${t.displayWeight} 16px/1.25 ${t.fontDisplay}`, color: c.ink }}>{b.t}</div>
                <div style={{ font: `400 12px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{b.a}</div>
                <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 8 }}>June 8, 2026 · 14:12</div>
              </div>
            </div>
          </div>

          <div style={{ padding: '22px 22px 0' }}>
            <Label t={t} c={c}>Payment</Label>
            <div style={{ ...cardStyle(t, c), padding: '13px 16px', marginTop: 11 }}>
              <PayPalRow t={t} c={c} trailing={<span style={{ font: `${t.displayWeight} 16px/1 ${t.fontDisplay}`, color: c.ink }}>$11.43</span>} />
            </div>
          </div>

          <div style={{ padding: '22px 22px 0' }}>
            <Label t={t} c={c}>Breakdown</Label>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 13 }}>
              <PriceRow t={t} c={c} label="Book price" val="$12.99" />
              <PriceRow t={t} c={c} label="Coins applied" val="−$2.40" accent />
              <PriceRow t={t} c={c} label="Tax" val="$0.84" />
              <div style={{ height: 1, background: c.line, margin: '2px 0' }} />
              <PriceRow t={t} c={c} label="Total charged" val="$11.43" strong />
            </div>
          </div>

          <div style={{ padding: '20px 22px 0', display: 'flex', justifyContent: 'center' }}>
            <span style={{ font: `600 12.5px/1 ${t.fontBody}`, color: c.muted }}>Need help with this order?</span>
          </div>

          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, padding: '14px 22px 18px', background: c.paper, borderTop: `1px solid ${c.line}`, display: 'flex', gap: 10 }}>
            <Btn t={t} c={c} kind="secondary" style={{ flex: 1 }}>Receipt</Btn>
            <Btn t={t} c={c} style={{ flex: 1.4 }}><Ic n="book_open" s={16} color={c.onAccent} />Read now</Btn>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Payment methods ---------- */
  function PaymentMethods({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={760}>
        <div style={{ height: '100%' }}>
          <TopBar t={t} c={c} title="Payment methods" />

          <div style={{ padding: '8px 22px 0', display: 'flex', flexDirection: 'column', gap: 12 }}>
            <div style={{ ...cardStyle(t, c), padding: '13px 16px', border: `1.5px solid ${c.accent}` }}>
              <PayPalRow t={t} c={c} trailing={<Radio c={c} on />} />
            </div>
            <div style={{ ...cardStyle(t, c), padding: '13px 16px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 13 }}>
                <div style={{ width: 42, height: 42, borderRadius: t.radiusSm, background: c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><CardGlyph c={c} /></div>
                <div style={{ flex: 1 }}>
                  <div style={{ font: `700 13.5px/1 ${t.fontBody}`, color: c.ink }}>Visa ·· 4129</div>
                  <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>Expires 08/27</div>
                </div>
                <Radio c={c} />
              </div>
            </div>
            <div style={{ ...cardStyle(t, c), padding: '13px 16px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: 13 }}>
                <div style={{ width: 42, height: 42, borderRadius: t.radiusSm, background: c.alt, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="apple" s={19} color={c.ink} /></div>
                <div style={{ flex: 1 }}>
                  <div style={{ font: `700 13.5px/1 ${t.fontBody}`, color: c.ink }}>Apple Pay</div>
                  <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 5 }}>Device wallet</div>
                </div>
                <Radio c={c} />
              </div>
            </div>

            {/* add new */}
            <div style={{ borderRadius: t.radius, border: `1.5px dashed ${c.line}`, padding: '15px 16px', display: 'flex', alignItems: 'center', gap: 13 }}>
              <div style={{ width: 34, height: 34, borderRadius: 999, background: c.accentSoft, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="plus" s={13} color={c.accent} /></div>
              <span style={{ font: `600 13px/1 ${t.fontBody}`, color: c.accent }}>Add payment method</span>
            </div>
          </div>

          <p style={{ font: `400 11.5px/1.65 ${t.fontBody}`, color: c.muted, margin: '20px 24px 0', display: 'flex', gap: 8 }}>
            <Ic n="lock" s={13} color={c.muted} style={{ marginTop: 1 }} />
            Payment details are encrypted and stored by our payment partner — never on DZ's servers.
          </p>
        </div>
      </Frame>
    );
  }

  /* ---------- Payment success ---------- */
  function PaymentSuccess({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const b = BOOKS[0];
    return (
      <Frame t={t} c={c} h={760}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '4px 26px 26px' }}>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}><IconBtn t={t} c={c} n="close" s={13} /></div>
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
            <div style={{ width: 76, height: 76, borderRadius: 999, background: c.accentSoft, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <div style={{ width: 54, height: 54, borderRadius: 999, background: c.accent, display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 12px 26px rgba(20,18,12,0.28)' }}>
                <Ic n="done" s={22} color={c.onAccent} />
              </div>
            </div>
            <div style={{ font: `${t.displayWeight} 28px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 26 }}>It's yours</div>
            <p style={{ font: `400 13.5px/1.65 ${t.fontBody}`, color: c.muted, margin: '12px 0 0', maxWidth: 250 }}>
              <b style={{ color: c.ink, fontWeight: 700 }}>{b.t}</b> is on your shelf. Receipt sent to amelia@hartwell.co.
            </p>
            <div style={{ ...cardStyle(t, c), padding: 14, marginTop: 24, display: 'flex', gap: 13, alignItems: 'center', alignSelf: 'stretch', textAlign: 'left' }}>
              <img src={b.c} alt="" style={{ width: 44, height: 64, borderRadius: t.cover - 1, objectFit: 'cover', boxShadow: '0 6px 14px rgba(20,18,12,0.2)' }} />
              <div style={{ flex: 1, minWidth: 0 }}>
                <div style={{ font: `${t.displayWeight} 14.5px/1.2 ${t.fontDisplay}`, color: c.ink }}>{b.t}</div>
                <div style={{ font: `400 11.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{b.a}</div>
              </div>
              <span style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted }}>$11.43</span>
            </div>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
            <Btn t={t} c={c}><Ic n="book_open" s={16} color={c.onAccent} />Start reading</Btn>
            <Btn t={t} c={c} kind="ghost" h={44}>Back to store</Btn>
          </div>
        </div>
      </Frame>
    );
  }

  /* ---------- Payment failed ---------- */
  function PaymentFailed({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const red = mode === 'D' ? '#D96A55' : '#B3402A';
    const redSoft = mode === 'D' ? '#3A241F' : '#F0DCD2';
    return (
      <Frame t={t} c={c} h={760}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column', padding: '4px 26px 26px' }}>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}><IconBtn t={t} c={c} n="close" s={13} /></div>
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
            <div style={{ width: 76, height: 76, borderRadius: 999, background: redSoft, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <div style={{ width: 54, height: 54, borderRadius: 999, border: `2px solid ${red}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Ic n="close" s={18} color={red} />
              </div>
            </div>
            <div style={{ font: `${t.displayWeight} 28px/1.15 ${t.fontDisplay}`, color: c.ink, marginTop: 26 }}>Payment didn't go through</div>
            <p style={{ font: `400 13.5px/1.65 ${t.fontBody}`, color: c.muted, margin: '12px 0 0', maxWidth: 260 }}>
              PayPal declined the charge. You haven't been billed — check your payment method and try again.
            </p>
            <div style={{ marginTop: 22, padding: '12px 16px', borderRadius: t.radiusSm, background: c.alt, font: `500 11.5px/1.5 ${t.fontBody}`, color: c.inkSoft, maxWidth: 260 }}>
              Error PP-4031 · insufficient balance
            </div>
          </div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
            <Btn t={t} c={c}>Try again</Btn>
            <Btn t={t} c={c} kind="secondary">Change payment method</Btn>
            <Btn t={t} c={c} kind="ghost" h={40}>Contact support</Btn>
          </div>
        </div>
      </Frame>
    );
  }

  window.DZInkPurchase = { PrePurchase, PurchaseConfirmation, PurchaseDetails, PaymentMethods, PaymentSuccess, PaymentFailed };
})();
