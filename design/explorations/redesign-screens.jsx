/* DZ redesign — screen components. One markup, three themes, light + dark.
   Exposes window.DZScreens2 = { Foundations, Home, Detail, Reading }. */
(function () {
  const COVER = '../assets/covers/';
  const AV = '../assets/avatars/';

  function Ic({ n, s = 20, color = 'currentColor', style }) {
    const def = (window.DZICO || {})[n];
    if (!def) return <span style={{ display: 'inline-block', width: s, height: s, flexShrink: 0, ...style }} />;
    return <span style={{ display: 'inline-flex', width: s, height: s, color, flexShrink: 0, ...style }} dangerouslySetInnerHTML={{ __html: `<svg width="${s}" height="${s}" viewBox="${def.vb}" fill="none" style="display:block">${def.p}</svg>` }} />;
  }

  const pal = (t, mode) => (mode === 'D' ? t.D : t.L);
  function cardStyle(t, c) {
    if (t.card === 'soft') return { background: c.surface, borderRadius: t.radius, boxShadow: '0 6px 22px rgba(20,18,12,0.07)' };
    if (t.card === 'rich') return { background: c.surface, borderRadius: t.radius, border: `1px solid ${c.line}`, boxShadow: '0 4px 16px rgba(20,18,12,0.05)' };
    return { background: c.surface, borderRadius: t.radius, border: `1px solid ${c.line}` }; // flat
  }
  const Label = ({ t, c, children }) => (
    <div style={{ font: `700 10.5px/1 ${t.fontBody}`, letterSpacing: '0.14em', textTransform: 'uppercase', color: c.muted }}>{children}</div>
  );

  function Frame({ t, c, h, children, statusDark }) {
    return (
      <div style={{ width: '100%', height: h, background: c.paper, color: c.ink, fontFamily: t.fontBody, position: 'relative', overflow: 'hidden', display: 'flex', flexDirection: 'column', borderRadius: 26 }}>
        <div style={{ height: 40, display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 22px', flexShrink: 0 }}>
          <span style={{ font: `700 13px/1 ${t.fontBody}`, color: c.ink }}>9:41</span>
          <span style={{ display: 'flex', gap: 5, alignItems: 'center', opacity: 0.85 }}>
            <Ic n="search" s={0} />
            <svg width="16" height="10" viewBox="0 0 16 10" fill={c.ink}><rect x="0" y="6" width="2.6" height="4" rx="1"/><rect x="4" y="4" width="2.6" height="6" rx="1"/><rect x="8" y="2" width="2.6" height="8" rx="1"/><rect x="12" y="0" width="2.6" height="10" rx="1"/></svg>
            <svg width="22" height="10" viewBox="0 0 22 10" fill="none"><rect x="0.5" y="0.5" width="18" height="9" rx="2.4" stroke={c.ink} opacity="0.4"/><rect x="2" y="2" width="14" height="6" rx="1.2" fill={c.ink}/><rect x="20" y="3" width="1.5" height="4" rx="0.7" fill={c.ink} opacity="0.5"/></svg>
          </span>
        </div>
        <div style={{ flex: 1, overflow: 'hidden' }}>{children}</div>
      </div>
    );
  }

  const BOOKS = [
    { t: 'Mexican Gothic', a: 'Silvia Moreno-Garcia', c: COVER + 'book_cover.png', g: ['Literary', 'Gothic'], price: '12.99', rating: '4.6', pages: 320 },
    { t: 'The Archer', a: 'Paulo Coelho', c: COVER + 'book_cover_2.png', g: ['Fable', 'Wisdom'], price: '8.50', rating: '4.4', pages: 160 },
    { t: 'Red at the Bone', a: 'Jacqueline Woodson', c: COVER + 'book_cover_3.png', g: ['Literary', 'Family'], price: '10.00', rating: '4.5', pages: 208 },
    { t: 'Bestiary', a: 'K-Ming Chang', c: COVER + 'book_cover_4.png', g: ['Myth', 'Debut'], price: '9.20', rating: '4.2', pages: 272 },
    { t: 'Olive, Again', a: 'Elizabeth Strout', c: COVER + 'olive_again_book.png', g: ['Literary', 'Quiet'], price: '13.00', rating: '4.3', pages: 304 },
  ];

  /* ---------------- Chip ---------------- */
  function Chip({ t, c, children, solid }) {
    return (
      <span style={{ display: 'inline-flex', alignItems: 'center', height: 26, padding: '0 12px', borderRadius: 999, font: `600 11px/1 ${t.fontBody}`,
        background: solid ? c.accentSoft : 'transparent', color: solid ? c.accent : c.inkSoft, border: solid ? 'none' : `1px solid ${c.line}` }}>{children}</span>
    );
  }

  /* ---------------- Bottom nav ---------------- */
  function Nav({ t, c, active = 'home' }) {
    const items = [['home', 'home'], ['search', 'search'], ['book', 'library'], ['shop', 'store'], ['user', 'you']];
    return (
      <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, height: 64, background: c.paper, borderTop: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'space-around', paddingBottom: 6 }}>
        {items.map(([icon, key]) => {
          const on = key === active;
          return (
            <div key={key} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 5 }}>
              <Ic n={icon} s={21} color={on ? c.accent : c.muted} />
              <span style={{ width: 4, height: 4, borderRadius: 999, background: on ? c.accent : 'transparent' }} />
            </div>
          );
        })}
      </div>
    );
  }

  /* ================= HOME ================= */
  function Home({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const cont = BOOKS[0];
    return (
      <Frame t={t} c={c} h={880}>
        <div style={{ height: '100%', paddingBottom: 64 }}>
          {/* header */}
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '6px 22px 18px' }}>
            <div>
              <div style={{ font: `500 12px/1 ${t.fontBody}`, color: c.muted, marginBottom: 6 }}>Good evening</div>
              <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink }}>Amelia</div>
            </div>
            <div style={{ width: 40, height: 40, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="search" s={18} color={c.inkSoft} /></div>
          </div>

          {/* continue reading */}
          <div style={{ padding: '0 22px' }}>
            <Label t={t} c={c}>Continue reading</Label>
            <div style={{ ...cardStyle(t, c), padding: 14, marginTop: 12, display: 'flex', gap: 14 }}>
              <img src={cont.c} alt="" style={{ width: 52, height: 76, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 6px 14px rgba(20,18,12,0.18)' }} />
              <div style={{ flex: 1, minWidth: 0, display: 'flex', flexDirection: 'column' }}>
                <div style={{ font: `${t.displayWeight} 16px/1.2 ${t.fontDisplay}`, color: c.ink, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{cont.t}</div>
                <div style={{ font: `400 12px/1.3 ${t.fontBody}`, color: c.muted, marginTop: 3 }}>{cont.a}</div>
                <div style={{ flex: 1 }} />
                <div style={{ height: 4, borderRadius: 999, background: c.alt, overflow: 'hidden', marginBottom: 7 }}><div style={{ width: '62%', height: '100%', background: c.accent, borderRadius: 999 }} /></div>
                <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted }}><span style={{ color: c.accent, fontWeight: 700 }}>62%</span> · 18 min left</div>
              </div>
            </div>
          </div>

          {/* For you */}
          <div style={{ padding: '26px 0 0' }}>
            <div style={{ display: 'flex', alignItems: 'baseline', justifyContent: 'space-between', padding: '0 22px', marginBottom: 14 }}>
              <div style={{ font: `${t.displayWeight} 18px/1 ${t.fontDisplay}`, color: c.ink }}>For you</div>
              <span style={{ font: `600 12px/1 ${t.fontBody}`, color: c.accent }}>See all</span>
            </div>
            <div style={{ display: 'flex', gap: 16, padding: '0 22px', overflow: 'hidden' }}>
              {BOOKS.slice(1, 4).map((b, i) => (
                <div key={i} style={{ width: 104, flexShrink: 0 }}>
                  <img src={b.c} alt="" style={{ width: 104, height: 152, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 8px 18px rgba(20,18,12,0.16)' }} />
                  <div style={{ font: `${t.displayWeight} 13px/1.25 ${t.fontDisplay}`, color: c.ink, marginTop: 9, overflow: 'hidden', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical' }}>{b.t}</div>
                  <div style={{ font: `400 11px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 3 }}>{b.a}</div>
                </div>
              ))}
            </div>
          </div>

          {/* Browse chips */}
          <div style={{ padding: '26px 22px 0' }}>
            <Label t={t} c={c}>Browse</Label>
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginTop: 12 }}>
              {['Literary', 'Fiction', 'History', 'Romance', 'Essays', 'Poetry'].map((g, i) => <Chip key={g} t={t} c={c} solid={i === 0}>{g}</Chip>)}
            </div>
          </div>

          {/* Trending list */}
          <div style={{ padding: '26px 22px 0' }}>
            <div style={{ font: `${t.displayWeight} 18px/1 ${t.fontDisplay}`, color: c.ink, marginBottom: 14 }}>Trending now</div>
            {BOOKS.slice(2, 4).map((b, i) => (
              <div key={i} style={{ display: 'flex', gap: 14, alignItems: 'center', padding: '13px 0', borderTop: i ? `1px solid ${c.line}` : 'none' }}>
                <img src={b.c} alt="" style={{ width: 46, height: 66, borderRadius: t.cover, objectFit: 'cover' }} />
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ font: `${t.displayWeight} 15px/1.2 ${t.fontDisplay}`, color: c.ink }}>{b.t}</div>
                  <div style={{ font: `400 12px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 3 }}>{b.a}</div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: 5, marginTop: 6 }}>
                    <Ic n="star" s={12} color={c.accent} /><span style={{ font: `600 11px/1 ${t.fontBody}`, color: c.inkSoft }}>{b.rating}</span>
                    <span style={{ color: c.muted, font: `400 11px/1 ${t.fontBody}` }}>· {b.g[0]}</span>
                  </div>
                </div>
                <Ic n="bookmark" s={17} color={c.muted} />
              </div>
            ))}
          </div>

          {/* From your circle */}
          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>From your circle</Label>
            <div style={{ marginTop: 12, display: 'flex', flexDirection: 'column', gap: 14 }}>
              {[['profile_2.png', 'Patricia', 'is reading Olive, Again', true], ['profile_3.png', 'Daniel', 'finished Bestiary', false]].map((f, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
                  <div style={{ position: 'relative' }}>
                    <img src={AV + f[0]} alt="" style={{ width: 38, height: 38, borderRadius: t.radiusSm, objectFit: 'cover' }} />
                    {f[3] && <span style={{ position: 'absolute', bottom: -2, right: -2, width: 11, height: 11, borderRadius: 999, background: c.accent, border: `2px solid ${c.paper}` }} />}
                  </div>
                  <div style={{ font: `400 13px/1.3 ${t.fontBody}`, color: c.inkSoft }}><b style={{ color: c.ink, fontWeight: 700 }}>{f[1]}</b> {f[2]}</div>
                </div>
              ))}
            </div>
          </div>
        </div>
        <Nav t={t} c={c} active="home" />
      </Frame>
    );
  }

  /* ================= BOOK DETAIL ================= */
  function Detail({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const b = BOOKS[0];
    return (
      <Frame t={t} c={c} h={850}>
        <div style={{ height: '100%', position: 'relative', paddingBottom: 86 }}>
          {/* top bar */}
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '4px 22px 8px' }}>
            <div style={{ width: 40, height: 40, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="back" s={18} color={c.ink} /></div>
            <div style={{ display: 'flex', gap: 10 }}>
              <div style={{ width: 40, height: 40, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="share" s={17} color={c.ink} /></div>
              <div style={{ width: 40, height: 40, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="bookmark" s={17} color={c.ink} /></div>
            </div>
          </div>

          {/* hero */}
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', padding: '18px 22px 0' }}>
            <img src={b.c} alt="" style={{ width: 132, height: 196, borderRadius: t.cover + 2, objectFit: 'cover', boxShadow: '0 18px 36px rgba(20,18,12,0.30)' }} />
            <div style={{ font: `${t.displayWeight} 25px/1.15 ${t.fontDisplay}`, color: c.ink, textAlign: 'center', marginTop: 22 }}>{b.t}</div>
            <div style={{ font: `400 13px/1.2 ${t.fontBody}`, color: c.muted, marginTop: 7 }}>{b.a}</div>
            <div style={{ display: 'flex', gap: 7, marginTop: 14 }}>{b.g.map((g) => <Chip key={g} t={t} c={c} solid>{g}</Chip>)}</div>
          </div>

          {/* meta strip */}
          <div style={{ display: 'flex', margin: '24px 22px 0', ...cardStyle(t, c), padding: '16px 0' }}>
            {[['Rating', b.rating + ' ★'], ['Pages', String(b.pages)], ['Time', '6h 20m'], ['Lang', 'EN']].map((m, i) => (
              <div key={i} style={{ flex: 1, textAlign: 'center', borderLeft: i ? `1px solid ${c.line}` : 'none' }}>
                <div style={{ font: `${t.displayWeight} 16px/1.1 ${t.fontDisplay}`, color: c.ink }}>{m[1]}</div>
                <div style={{ font: `500 10px/1 ${t.fontBody}`, letterSpacing: '0.08em', textTransform: 'uppercase', color: c.muted, marginTop: 5 }}>{m[0]}</div>
              </div>
            ))}
          </div>

          {/* about */}
          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>About</Label>
            <p style={{ font: `400 14px/1.75 ${t.fontBody}`, color: c.inkSoft, margin: '12px 0 0' }}>
              An atmospheric, slow-burning novel that pulls you into a crumbling house and the secrets it keeps. Richly drawn and quietly unsettling — a favourite among the DZ reading circle.
            </p>
          </div>

          {/* also by */}
          <div style={{ padding: '22px 0 0' }}>
            <div style={{ font: `${t.displayWeight} 16px/1 ${t.fontDisplay}`, color: c.ink, padding: '0 22px', marginBottom: 12 }}>More like this</div>
            <div style={{ display: 'flex', gap: 12, padding: '0 22px', overflow: 'hidden' }}>
              {BOOKS.slice(1, 5).map((x, i) => <img key={i} src={x.c} alt="" style={{ width: 64, height: 94, borderRadius: t.cover, objectFit: 'cover', flexShrink: 0, boxShadow: '0 6px 14px rgba(20,18,12,0.14)' }} />)}
            </div>
          </div>

          {/* buy bar */}
          <div style={{ position: 'absolute', left: 0, right: 0, bottom: 0, padding: '14px 22px 18px', background: c.paper, borderTop: `1px solid ${c.line}`, display: 'flex', alignItems: 'center', gap: 14 }}>
            <div>
              <div style={{ font: `500 10px/1 ${t.fontBody}`, letterSpacing: '0.06em', textTransform: 'uppercase', color: c.muted }}>Price</div>
              <div style={{ font: `${t.displayWeight} 22px/1.1 ${t.fontDisplay}`, color: c.ink, marginTop: 4 }}>${b.price}</div>
            </div>
            <button style={{ flex: 1, height: 52, borderRadius: t.radiusSm + 2, border: 'none', background: c.accent, color: c.onAccent, font: `700 15px/1 ${t.fontBody}`, cursor: 'pointer' }}>Buy now</button>
            <button style={{ width: 52, height: 52, borderRadius: t.radiusSm + 2, border: `1px solid ${c.line}`, background: 'transparent', color: c.ink, cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}><Ic n="book_open" s={20} color={c.ink} /></button>
          </div>
        </div>
      </Frame>
    );
  }

  /* ================= READING (dark) ================= */
  function Reading({ t, mode = 'D' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={720}>
        <div style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '4px 22px 10px' }}>
            <Ic n="back" s={20} color={c.muted} />
            <div style={{ textAlign: 'center' }}>
              <div style={{ font: `500 11px/1.2 ${t.fontBody}`, color: c.muted, letterSpacing: '0.04em' }}>MEXICAN GOTHIC</div>
            </div>
            <div style={{ display: 'flex', gap: 16 }}><Ic n="text_size" s={19} color={c.muted} /><Ic n="bookmark" s={18} color={c.muted} /></div>
          </div>

          <div style={{ flex: 1, padding: '14px 26px 0', overflow: 'hidden' }}>
            <div style={{ font: `400 13px/1 ${t.fontAccent}`, color: c.accent, marginBottom: 14, fontStyle: 'italic' }}>Chapter Three</div>
            <div style={{ font: `${t.displayWeight} 22px/1.25 ${t.fontDisplay}`, color: c.ink, marginBottom: 20 }}>The Arrival</div>
            <p style={{ font: `400 16.5px/1.85 ${t.fontAccent}`, color: c.ink, margin: '0 0 16px', opacity: 0.92 }}>
              The house appeared out of the mist like something half-remembered from a dream — tall, severe, and utterly silent. Noemí pressed her face to the carriage window and watched the iron gates draw closer.
            </p>
            <p style={{ font: `400 16.5px/1.85 ${t.fontAccent}`, color: c.ink, margin: '0 0 16px', opacity: 0.92 }}>
              She had not wanted to come. The city, with its parties and its noise, was where she belonged. Yet the letter had been impossible to ignore.
            </p>
            <p style={{ font: `400 16.5px/1.85 ${t.fontAccent}`, color: c.ink, margin: 0, opacity: 0.92 }}>
              “We are nearly there,” the driver said, though his voice carried no comfort at all.
            </p>
          </div>

          <div style={{ padding: '16px 22px 22px', borderTop: `1px solid ${c.line}` }}>
            <div style={{ height: 4, borderRadius: 999, background: c.alt, overflow: 'hidden', marginBottom: 12 }}><div style={{ width: '62%', height: '100%', background: c.accent, borderRadius: 999 }} /></div>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
              <Ic n="back" s={18} color={c.muted} />
              <span style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted }}>Page 42 of 320 · <span style={{ color: c.accent }}>62%</span></span>
              <span style={{ transform: 'rotate(180deg)', display: 'flex' }}><Ic n="back" s={18} color={c.muted} /></span>
            </div>
          </div>
        </div>
      </Frame>
    );
  }

  /* ================= FOUNDATIONS ================= */
  function Foundations({ t }) {
    const c = t.L, d = t.D;
    const sw = (bg, label, border) => (
      <div style={{ flex: 1 }}>
        <div style={{ height: 46, borderRadius: t.radiusSm, background: bg, border: border ? `1px solid ${c.line}` : 'none' }} />
        <div style={{ font: `500 9.5px/1.3 ${t.fontBody}`, color: c.muted, marginTop: 6 }}>{label}</div>
      </div>
    );
    return (
      <div style={{ width: '100%', height: '100%', background: c.paper, color: c.ink, fontFamily: t.fontBody, padding: 26, display: 'flex', flexDirection: 'column', gap: 18 }}>
        <div>
          <div style={{ font: `${t.displayWeight} 28px/1 ${t.fontDisplay}`, color: c.ink }}>{t.name}</div>
          <div style={{ font: `400 12.5px/1 ${t.fontBody}`, color: c.muted, marginTop: 7 }}>{t.tagline}</div>
        </div>

        <div style={{ display: 'flex', gap: 8 }}>
          {sw(c.paper, 'Paper', true)}{sw(c.surface, 'Surface', true)}{sw(c.ink, 'Ink')}{sw(c.accent, 'Accent')}{sw(c.accentSoft, 'Accent soft', true)}
        </div>
        <div style={{ display: 'flex', gap: 8 }}>
          {sw(d.paper, 'Dark paper')}{sw(d.surface, 'Dark surf')}{sw(d.ink, 'Dark ink')}{sw(d.accent, 'Dark accent')}{sw(c.line, 'Line', true)}
        </div>

        {/* type */}
        <div style={{ display: 'flex', gap: 18, alignItems: 'flex-end', borderTop: `1px solid ${c.line}`, paddingTop: 16 }}>
          <div style={{ font: `${t.displayWeight} 52px/0.9 ${t.fontDisplay}`, color: c.ink }}>Ag</div>
          <div style={{ flex: 1 }}>
            <div style={{ font: `${t.displayWeight} 19px/1.2 ${t.fontDisplay}`, color: c.ink }}>Display · {t.fontDisplay.split("'")[1]}</div>
            <div style={{ font: `400 13px/1.5 ${t.fontBody}`, color: c.inkSoft, marginTop: 4 }}>Body · {t.fontBody.split("'")[1]} — calm, readable, premium reading.</div>
          </div>
        </div>

        {/* components */}
        <div style={{ display: 'flex', gap: 10, alignItems: 'center', borderTop: `1px solid ${c.line}`, paddingTop: 16 }}>
          <button style={{ height: 44, padding: '0 20px', borderRadius: t.radiusSm + 2, border: 'none', background: c.accent, color: c.onAccent, font: `700 13px/1 ${t.fontBody}`, cursor: 'pointer' }}>Buy now</button>
          <button style={{ height: 44, padding: '0 18px', borderRadius: t.radiusSm + 2, border: `1px solid ${c.line}`, background: 'transparent', color: c.ink, font: `600 13px/1 ${t.fontBody}`, cursor: 'pointer' }}>Sample</button>
          <Chip t={t} c={c} solid>Literary</Chip>
          <Chip t={t} c={c}>Essays</Chip>
        </div>
        <div style={{ display: 'flex', gap: 10 }}>
          <div style={{ flex: 1, height: 46, borderRadius: t.radiusSm, border: `1px solid ${c.line}`, background: c.surface, display: 'flex', alignItems: 'center', gap: 10, padding: '0 14px' }}>
            <Ic n="search" s={17} color={c.muted} /><span style={{ font: `400 13px/1 ${t.fontBody}`, color: c.muted }}>Search books, authors…</span>
          </div>
        </div>
      </div>
    );
  }

  window.DZScreens2 = { Foundations, Home, Detail, Reading };
})();
