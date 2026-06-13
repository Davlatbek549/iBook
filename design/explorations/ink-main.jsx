/* DZ Ink & Paper — main tab screens (Home, Detail, Reading live in redesign-screens.jsx).
   Exposes window.DZInkMain = { Search, Library, Store } */
(function () {
  const { Ic, pal, cardStyle, Frame, Nav, Chip, Label, IconBtn, Btn, Field, SectionTitle, BookRow, Stars, BOOKS, CATS } = window.DZInk;

  /* ---------- Search ---------- */
  function Search({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const recent = ['olive again', 'paulo coelho', 'gothic novels'];
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%', paddingBottom: 64 }}>
          <div style={{ padding: '6px 22px 0' }}>
            <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink }}>Search</div>
            <div style={{ marginTop: 16 }}>
              <Field t={t} c={c} icon="search" placeholder="Books, authors, friends…" focus />
            </div>
          </div>

          <div style={{ padding: '24px 22px 0' }}>
            <Label t={t} c={c}>Recent</Label>
            <div style={{ marginTop: 6 }}>
              {recent.map((r, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '11px 0', borderBottom: i < recent.length - 1 ? `1px solid ${c.line}` : 'none' }}>
                  <Ic n="search" s={14} color={c.muted} />
                  <span style={{ flex: 1, font: `400 13.5px/1 ${t.fontBody}`, color: c.inkSoft }}>{r}</span>
                  <Ic n="close" s={11} color={c.muted} />
                </div>
              ))}
            </div>
          </div>

          <div style={{ padding: '22px 22px 0' }}>
            <SectionTitle t={t} c={c}>Browse by mood</SectionTitle>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 10, marginTop: 14 }}>
              {CATS.map((cat, i) => (
                <div key={cat} style={{ ...cardStyle(t, c), background: i % 3 === 0 ? c.alt : c.surface, padding: '16px 15px', display: 'flex', flexDirection: 'column', gap: 22 }}>
                  <span style={{ font: `400 11px/1 ${t.fontAccent}`, fontStyle: 'italic', color: c.muted }}>{String(i + 1).padStart(2, '0')}</span>
                  <span style={{ font: `${t.displayWeight} 15px/1.2 ${t.fontDisplay}`, color: c.ink }}>{cat}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
        <Nav t={t} c={c} active="search" />
      </Frame>
    );
  }

  /* ---------- Library ---------- */
  function Library({ t, mode = 'L' }) {
    const c = pal(t, mode);
    const tabs = ['Reading', 'To read', 'Finished'];
    const reading = [
      { b: BOOKS[0], p: 62, left: '18 min left' },
      { b: BOOKS[4], p: 31, left: '2h 40m left' },
      { b: BOOKS[1], p: 88, left: '12 min left' },
    ];
    return (
      <Frame t={t} c={c} h={840}>
        <div style={{ height: '100%', paddingBottom: 64 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '6px 22px 0' }}>
            <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink }}>Library</div>
            <IconBtn t={t} c={c} n="grid_view" s={16} />
          </div>

          <div style={{ display: 'flex', gap: 22, padding: '20px 22px 0', borderBottom: `1px solid ${c.line}` }}>
            {tabs.map((tab, i) => (
              <div key={tab} style={{ paddingBottom: 11, borderBottom: i === 0 ? `2px solid ${c.accent}` : '2px solid transparent' }}>
                <span style={{ font: `${i === 0 ? 700 : 500} 13.5px/1 ${t.fontBody}`, color: i === 0 ? c.ink : c.muted }}>{tab}</span>
              </div>
            ))}
          </div>

          <div style={{ padding: '6px 22px 0' }}>
            {reading.map((r, i) => (
              <BookRow key={i} t={t} c={c} b={r.b} top={i > 0}
                meta={
                  <div>
                    <div style={{ height: 4, borderRadius: 999, background: c.alt, overflow: 'hidden', width: '85%' }}><div style={{ width: r.p + '%', height: '100%', background: c.accent, borderRadius: 999 }} /></div>
                    <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 6 }}><span style={{ color: c.accent, fontWeight: 700 }}>{r.p}%</span> · {r.left}</div>
                  </div>
                }
                trailing={<Ic n="three_vertical_dots" s={16} color={c.muted} />} />
            ))}
          </div>

          <div style={{ padding: '20px 22px 0' }}>
            <SectionTitle t={t} c={c} action="See all">Collections</SectionTitle>
            <div style={{ display: 'flex', gap: 12, marginTop: 14 }}>
              {[['Quiet novels', 12, [BOOKS[4], BOOKS[2]]], ['For the train', 5, [BOOKS[1], BOOKS[3]]]].map(([name, n, bs], i) => (
                <div key={i} style={{ flex: 1, ...cardStyle(t, c), padding: 13 }}>
                  <div style={{ display: 'flex', gap: 7 }}>
                    {bs.map((b, j) => <img key={j} src={b.c} alt="" style={{ width: 40, height: 58, borderRadius: t.cover - 1, objectFit: 'cover' }} />)}
                  </div>
                  <div style={{ font: `${t.displayWeight} 13.5px/1.2 ${t.fontDisplay}`, color: c.ink, marginTop: 11 }}>{name}</div>
                  <div style={{ font: `400 11px/1 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{n} books</div>
                </div>
              ))}
            </div>
          </div>

          <div style={{ padding: '20px 22px 0' }}>
            <div style={{ ...cardStyle(t, c), background: c.alt, border: 'none', padding: '14px 16px', display: 'flex', alignItems: 'center', gap: 12 }}>
              <Ic n="stats" s={18} color={c.accent} />
              <div style={{ flex: 1, font: `400 12.5px/1.4 ${t.fontBody}`, color: c.inkSoft }}>You've read <b style={{ color: c.ink, fontWeight: 700 }}>26 min</b> today — 4 min to your goal.</div>
            </div>
          </div>
        </div>
        <Nav t={t} c={c} active="library" />
      </Frame>
    );
  }

  /* ---------- Store ---------- */
  function Store({ t, mode = 'L' }) {
    const c = pal(t, mode);
    return (
      <Frame t={t} c={c} h={860}>
        <div style={{ height: '100%', paddingBottom: 64 }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '6px 22px 0' }}>
            <div style={{ font: `${t.displayWeight} 24px/1 ${t.fontDisplay}`, color: c.ink }}>Store</div>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, height: 32, padding: '0 13px', borderRadius: 999, border: `1px solid ${c.line}` }}>
              <Ic n="tag" s={14} color={c.accent} />
              <span style={{ font: `700 12px/1 ${t.fontBody}`, color: c.ink }}>240</span>
            </div>
          </div>

          {/* featured */}
          <div style={{ margin: '18px 22px 0', ...cardStyle(t, c), background: c.alt, border: 'none', padding: 18, display: 'flex', gap: 16, alignItems: 'center' }}>
            <img src={BOOKS[2].c} alt="" style={{ width: 84, height: 124, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 14px 26px rgba(20,18,12,0.25)' }} />
            <div style={{ flex: 1, minWidth: 0 }}>
              <Label t={t} c={c} style={{ color: c.accent }}>Book of the week</Label>
              <div style={{ font: `${t.displayWeight} 19px/1.2 ${t.fontDisplay}`, color: c.ink, marginTop: 8 }}>{BOOKS[2].t}</div>
              <div style={{ font: `400 12px/1.3 ${t.fontBody}`, color: c.muted, marginTop: 4 }}>{BOOKS[2].a}</div>
              <div style={{ display: 'flex', alignItems: 'center', gap: 10, marginTop: 12 }}>
                <span style={{ font: `${t.displayWeight} 16px/1 ${t.fontDisplay}`, color: c.ink }}>$7.00</span>
                <span style={{ font: `400 12px/1 ${t.fontBody}`, color: c.muted, textDecoration: 'line-through' }}>$10.00</span>
                <Chip t={t} c={c} solid>−30%</Chip>
              </div>
            </div>
          </div>

          {/* new releases rail */}
          <div style={{ padding: '24px 0 0' }}>
            <SectionTitle t={t} c={c} action="See all" style={{ padding: '0 22px', marginBottom: 14 }}>New releases</SectionTitle>
            <div style={{ display: 'flex', gap: 16, padding: '0 22px', overflow: 'hidden' }}>
              {[BOOKS[1], BOOKS[3], BOOKS[4]].map((b, i) => (
                <div key={i} style={{ width: 104, flexShrink: 0 }}>
                  <img src={b.c} alt="" style={{ width: 104, height: 152, borderRadius: t.cover, objectFit: 'cover', boxShadow: '0 8px 18px rgba(20,18,12,0.16)' }} />
                  <div style={{ font: `${t.displayWeight} 13px/1.25 ${t.fontDisplay}`, color: c.ink, marginTop: 9, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{b.t}</div>
                  <div style={{ font: `700 12px/1 ${t.fontBody}`, color: c.accent, marginTop: 5 }}>${b.price}</div>
                </div>
              ))}
            </div>
          </div>

          {/* top sellers list */}
          <div style={{ padding: '24px 22px 0' }}>
            <SectionTitle t={t} c={c}>Top sellers</SectionTitle>
            <div style={{ marginTop: 4 }}>
              {[BOOKS[0], BOOKS[4], BOOKS[3]].map((b, i) => (
                <BookRow key={i} t={t} c={c} b={b} top={i > 0}
                  meta={<div style={{ display: 'flex', alignItems: 'center', gap: 6 }}><Stars c={c} n={4} s={11} /><span style={{ font: `600 11px/1 ${t.fontBody}`, color: c.inkSoft }}>{b.rating}</span></div>}
                  trailing={<span style={{ font: `${t.displayWeight} 15px/1 ${t.fontDisplay}`, color: c.ink }}>${b.price}</span>} />
              ))}
            </div>
          </div>
        </div>
        <Nav t={t} c={c} active="store" />
      </Frame>
    );
  }

  window.DZInkMain = { Search, Library, Store };
})();
